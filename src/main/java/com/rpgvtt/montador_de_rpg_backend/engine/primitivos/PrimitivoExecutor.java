package com.rpgvtt.montador_de_rpg_backend.engine.primitivos;

import com.rpgvtt.montador_de_rpg_backend.domain.model.entidade.EntidadeInstancia;
import com.rpgvtt.montador_de_rpg_backend.domain.model.sistema.Sistema;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.EtapaAvulsa;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.ContextoAvulso;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.contexto.ResultadoEtapa;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.EtapaExecutavel;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.EtapaHandler;
import com.rpgvtt.montador_de_rpg_backend.engine.procedimentos.interfaces.ExecucaoContexto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrimitivoExecutor {

    private final HandlerRegistry registry;
    private final JsonMapper mapper;

    public ResultadoEtapa executarPrimitivoAvulso(String tipoPrimitivo,
                                                  Sistema sistema,
                                                  EntidadeInstancia executor,
                                                  EntidadeInstancia alvo,
                                                  Map<String, Object> parametros) {

        EtapaHandler handler = registry.get(tipoPrimitivo);
        if (handler == null) {
            return ResultadoEtapa.erro("Handler de primitivo não encontrado: " + tipoPrimitivo);
        }

        // Garante que o handler atue sobre o ALVO correto, sem depender
        // de "instância ativa" — segue o mesmo padrão de override
        // já usado por AlterarAtributoHandler/CalcularAtributoHandler.
        Map<String, Object> paramsComAlvo = new HashMap<>(parametros);
        paramsComAlvo.putIfAbsent("id_entidade", alvo.getId());

        EtapaExecutavel etapaAvulsa = EtapaAvulsa.de(tipoPrimitivo, paramsComAlvo, mapper);
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
