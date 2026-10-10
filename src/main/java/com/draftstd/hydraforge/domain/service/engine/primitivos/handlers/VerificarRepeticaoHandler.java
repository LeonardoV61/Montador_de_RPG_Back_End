package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import com.draftstd.hydraforge.domain.service.engine.components.InterpretadorJson;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InterpretadorContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ProcedimentoContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.domain.service.engine.utils.interpretador.contexto.Contexto;
import com.draftstd.hydraforge.adapters.out.repository.sessao.CenaRepository;
import com.draftstd.hydraforge.domain.service.engine.utils.interpretador.ResultadoExpressao;
// import com.draftstd.hydraforge.adapters.out.repository.batalha.cenaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class VerificarRepeticaoHandler implements EtapaHandler {

    private final InterpretadorJson interpretador;
    private final InstanciaResolver instanciaResolver;
    private final CenaRepository cenaRepo;
    private final JsonMapper mapper;

    @Override
    public String tipoEtapa() {
        return "VERIFICAR_REPETICAO";
    }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        return null;
    }

    public ResultadoEtapa executar(EtapaProcedimento etapa, ProcedimentoContexto ctx) {
        // parametros_etapa is already JsonNode — no objectMapper needed
        JsonNode params = etapa.getParametrosEtapa(); // returns raw JsonNode

        int maxIteracoes = params.path("max_iteracoes").asInt(500);
        if (ctx.getEtapaAtual() >= maxIteracoes) {
            return ResultadoEtapa.erro(
                    "Limite de iterações atingido (" + maxIteracoes + ") em '" +
                            ctx.getProcedimento().getNome() + "'");
        }

        JsonNode expressaoCondicao = params.get("condicao");
        if (expressaoCondicao == null) {
            return ResultadoEtapa.erro("REPETIR_SE sem campo 'condicao'");
        }

        Contexto contexto = new InterpretadorContexto(ctx, instanciaResolver, cenaRepo, mapper);
        ResultadoExpressao resultado = interpretador.interpretar(expressaoCondicao, contexto);

        if (resultado.getTipo() != ResultadoExpressao.TipoResultado.BOOLEANO) {
            return ResultadoEtapa.erro(
                    "REPETIR_SE: condicao deve retornar booleano, retornou "
                            + resultado.getTipo());
        }

        if (!resultado.comoBooleano()) {
            return ResultadoEtapa.concluida(Map.of(
                    "loop_encerrado",  true,
                    "total_iteracoes", ctx.getEtapaAtual()
            ));
        }

        // Clean transient keys
        JsonNode limparNode = params.get("limpar_contexto");
        if (limparNode != null && limparNode.isArray()) {
            limparNode.forEach(n -> ctx.getContexto().remove(n.asString()));
        }

        int ordemAlvo  = params.get("voltar_para_ordem").asInt();
        int indiceAlvo = ctx.indiceParaOrdem(ordemAlvo);
        ctx.setEtapaAtual(indiceAlvo);
        ctx.incrementarIteracao();

        return ResultadoEtapa.repetindo(Map.of(
                "iteracao",            ctx.getIteracaoAtual(),
                "voltando_para_ordem", ordemAlvo
        ));
    }
}
