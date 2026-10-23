package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import com.draftstd.hydraforge.domain.service.engine.components.InterpretadorJson;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InstanciaResolver;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.InterpretadorContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ProcedimentoContexto;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;
import com.draftstd.hydraforge.ports.out.persistence.sessao.CenaPersistencePort;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Component
public class AcumularValorHandler implements EtapaHandler {

    private final InterpretadorJson interpretador;
    private final InstanciaResolver instanciaResolver;
    private final CenaPersistencePort cenaPort;
    private final JsonMapper mapper;

    public AcumularValorHandler(InterpretadorJson interpretador, 
                                InstanciaResolver instanciaResolver, 
                                CenaPersistencePort cenaPort,
                                JsonMapper mapper) {
        this.interpretador = interpretador;
        this.instanciaResolver = instanciaResolver;
        this.cenaPort = cenaPort;
        this.mapper = mapper;
    }

    @Override
    public String tipoEtapa() { return "ACUMULAR_VALOR"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        Map<String, Object> params = etapa.getParametrosEtapa();
        Object origemObj = params.get("origem");
        String destino = (String) params.get("destino");

        Object valor;
        if (origemObj instanceof JsonNode origemNode && origemNode.isObject()) {
            InterpretadorContexto intCtx = new InterpretadorContexto(
                (ProcedimentoContexto) ctx, instanciaResolver, cenaPort, mapper);
            valor = interpretador.interpretar(origemNode, intCtx).getValor();
        } else if (origemObj instanceof String origemStr) {
            valor = ctx.getContexto().get(origemStr, Object.class).orElse(0);
        } else {
            throw new IllegalArgumentException("'origem' deve ser texto ou nó de expressão JSON");
        }

        List<Object> lista = ctx.getContexto().getList(destino);
        lista.add(valor);
        return ResultadoEtapa.concluida(Map.of("status", "adicionado", "valor", valor, "tamanho", lista.size()));
    }
}