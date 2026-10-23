package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import com.draftstd.hydraforge.domain.service.engine.components.InterpretadorJson;
import com.draftstd.hydraforge.domain.service.engine.exceptions.JsonFieldNotFoundException;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InterpretadorContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ProcedimentoContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.domain.service.engine.utils.interpretador.ResultadoExpressao;
import com.draftstd.hydraforge.ports.out.persistence.sessao.CenaPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DefinirVariavelHandler implements EtapaHandler {

    private final InterpretadorJson interpretador;
    private final InstanciaResolver instanciaResolver;
    private final CenaPersistencePort cenaPort;
    private final JsonMapper mapper;

    @Override
    public String tipoEtapa() { return "DEFINIR_VARIAVEL"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();
        String nomeVariavel = exigirTexto(params, "nome", etapa);
        
        Object rawExpressao = params.get("expressao");
        if (rawExpressao == null) {
            throw new JsonFieldNotFoundException("expressao", etapa.getNome());
        }

        JsonNode expressaoNode = rawExpressao instanceof JsonNode node 
                ? node 
                : mapper.valueToTree(rawExpressao);

        // Cria o contexto de avaliação do Interpretador
        InterpretadorContexto intCtx = new InterpretadorContexto(
                (ProcedimentoContexto) ctx, 
                instanciaResolver, 
                cenaPort, 
                mapper
        );
        
        // Avalia a expressão JSON
        ResultadoExpressao resultado = interpretador.interpretar(expressaoNode, intCtx);

        // Salva o valor no contexto do procedimento com base no tipo retornado
        switch (resultado.getTipo()) {
            case NUMERO -> ctx.getContexto().put(nomeVariavel, resultado.comoNumero());
            case TEXTO -> ctx.getContexto().put(nomeVariavel, resultado.comoTexto());
            case BOOLEANO -> ctx.getContexto().put(nomeVariavel, resultado.comoBooleano());
            case LISTA -> ctx.getContexto().put(nomeVariavel, resultado.comoLista());
            case OBJETO -> ctx.getContexto().put(nomeVariavel, resultado.getValor());
            default -> ctx.getContexto().put(nomeVariavel, null);
        }

        return ResultadoEtapa.concluida(Map.of(
            "variavel", nomeVariavel,
            "valor_salvo", resultado.getValor()
        ));
    }

    private String exigirTexto(Map<String, Object> params, String chave, EtapaExecutavel etapa) {
        Object val = params.get(chave);
        if (val == null) throw new JsonFieldNotFoundException(chave, etapa.getNome());
        return val.toString();
    }
}