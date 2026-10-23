package com.draftstd.hydraforge.domain.service.engine.primitivos;

import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.EtapaAvulsa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ContextoAvulso;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimitivoExecutor {

    private final HandlerRegistry registry;

    public PrimitivoExecutor(HandlerRegistry registry) {
        this.registry = registry;
    }

    public ResultadoEtapa executarPrimitivoAvulso(String tipoPrimitivo,
                                                  Sistema sistema,
                                                  EntidadeInstancia executor,
                                                  EntidadeInstancia alvo,
                                                  Map<String, Object> parametros) {

        EtapaHandler handler = registry.get(tipoPrimitivo);
        if (handler == null) {
            return ResultadoEtapa.erro("Handler de primitivo não encontrado: " + tipoPrimitivo);
        }

        Map<String, Object> paramsComAlvo = new HashMap<>(parametros);
        paramsComAlvo.putIfAbsent("id_entidade", alvo.getId());

        EtapaExecutavel etapaAvulsa = EtapaAvulsa.de(tipoPrimitivo, paramsComAlvo);
        ExecucaoContexto ctxAvulso = new ContextoAvulso(sistema, executor, alvo);

        return handler.executar(etapaAvulsa, ctxAvulso);
    }

    public List<ResultadoEtapa> executarPrimitivosAvulsos(List<Map<String, Object>> definicoes,
                                                          Sistema sistema,
                                                          EntidadeInstancia executor,
                                                          EntidadeInstancia alvo) {
        List<ResultadoEtapa> resultados = new ArrayList<>();
        for (Map<String, Object> def : definicoes) {
            String tipo = (String) def.get("primitivo");
            @SuppressWarnings("unchecked")
            Map<String, Object> params = (Map<String, Object>) def.getOrDefault("parametros", Map.of());
            resultados.add(executarPrimitivoAvulso(tipo, sistema, executor, alvo, new HashMap<>(params)));
        }
        return resultados;
    }
}