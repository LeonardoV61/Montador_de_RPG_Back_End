package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.service.engine.components.InterpretadorJson;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InterpretadorContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ProcedimentoContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.domain.service.engine.utils.interpretador.ResultadoExpressao;
import com.draftstd.hydraforge.domain.service.engine.utils.interpretador.contexto.Contexto;
import com.draftstd.hydraforge.ports.out.persistence.sessao.CenaPersistencePort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.Collection;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class VerificarRepeticaoHandler implements EtapaHandler {

    private final InterpretadorJson interpretador;
    private final InstanciaResolver instanciaResolver;
    private final CenaPersistencePort cenaPort;
    private final JsonMapper mapper;

    @Override
    public String tipoEtapa() {
        return "VERIFICAR_REPETICAO";
    }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        if (!(ctx instanceof ProcedimentoContexto procedimentoCtx)) {
            return ResultadoEtapa.erro("VERIFICAR_REPETICAO requer um contexto de procedimento ativo");
        }

        Map<String, Object> params = etapa.getParametrosEtapa();

        Object rawMaxIteracoes = params.get("max_iteracoes");
        int maxIteracoes = rawMaxIteracoes instanceof Number n ? n.intValue()
                : (rawMaxIteracoes != null ? Integer.parseInt(rawMaxIteracoes.toString()) : 500);

        if (procedimentoCtx.getEtapaAtual() >= maxIteracoes) {
            return ResultadoEtapa.erro(
                    "Limite de iterações atingido (" + maxIteracoes + ") em '" +
                            procedimentoCtx.getProcedimento().getNome() + "'");
        }

        Object expressaoObj = params.get("condicao");
        if (expressaoObj == null) {
            return ResultadoEtapa.erro("REPETIR_SE sem campo 'condicao'");
        }

        JsonNode expressaoCondicao = mapper.valueToTree(expressaoObj);
        Contexto contexto = new InterpretadorContexto(procedimentoCtx, instanciaResolver, cenaPort, mapper);
        ResultadoExpressao resultado = interpretador.interpretar(expressaoCondicao, contexto);

        if (resultado.getTipo() != ResultadoExpressao.TipoResultado.BOOLEANO) {
            return ResultadoEtapa.erro(
                    "REPETIR_SE: condicao deve retornar booleano, retornou "
                            + resultado.getTipo());
        }

        if (!resultado.comoBooleano()) {
            return ResultadoEtapa.concluida(Map.of(
                    "loop_encerrado",  true,
                    "total_iteracoes", procedimentoCtx.getEtapaAtual()
            ));
        }

        // Limpa chaves transitórias do contexto
        Object limparObj = params.get("limpar_contexto");
        if (limparObj instanceof Collection<?> colecao) {
            colecao.forEach(item -> {
                if (item != null) procedimentoCtx.getContexto().remove(item.toString());
            });
        } else if (limparObj != null) {
            JsonNode limparNode = mapper.valueToTree(limparObj);
            if (limparNode.isArray()) {
                limparNode.forEach(n -> procedimentoCtx.getContexto().remove(n.asText()));
            }
        }

        Object rawOrdemAlvo = params.get("voltar_para_ordem");
        if (rawOrdemAlvo == null) {
            return ResultadoEtapa.erro("REPETIR_SE sem campo 'voltar_para_ordem'");
        }
        int ordemAlvo = rawOrdemAlvo instanceof Number n ? n.intValue() : Integer.parseInt(rawOrdemAlvo.toString());

        int indiceAlvo = procedimentoCtx.indiceParaOrdem(ordemAlvo);
        procedimentoCtx.setEtapaAtual(indiceAlvo);
        procedimentoCtx.incrementarIteracao();

        return ResultadoEtapa.repetindo(Map.of(
                "iteracao",            procedimentoCtx.getIteracaoAtual(),
                "voltando_para_ordem", ordemAlvo
        ));
    }
}