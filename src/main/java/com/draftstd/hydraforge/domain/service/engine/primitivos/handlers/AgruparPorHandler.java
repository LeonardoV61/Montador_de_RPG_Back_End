package com.draftstd.hydraforge.domain.service.engine.primitivos.handlers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.draftstd.hydraforge.domain.service.engine.components.InterpretadorJson;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.contexto.ResultadoEtapa;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaExecutavel;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;
import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.ExecucaoContexto;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.JsonNode;

@Component
@RequiredArgsConstructor
public class AgruparPorHandler implements EtapaHandler {
    private final InterpretadorJson interpretador;
    // ... dependências para InterpretadorContexto

    @Override
    public String tipoEtapa() { return "AGRUPAR_POR"; }

    @Override
    public ResultadoEtapa executar(EtapaExecutavel etapa, ExecucaoContexto ctx) {
        JsonNode params = etapa.getParametrosEtapa();
        String listaOrigem = params.get("lista").asString();
        String chave = params.get("chave").asString();
        String destino = params.get("destino").asString();

        List<Object> lista = ctx.getContexto().getList(listaOrigem);
        Map<Object, List<Object>> grupos = lista.stream()
            .filter(Map.class::isInstance)
            .collect(Collectors.groupingBy(item -> ((Map<?, ?>) item).get(chave), LinkedHashMap::new, Collectors.toList()));

        // Converte para lista de objetos {alvo, atacantes}
        List<Map<String, Object>> gruposList = grupos.entrySet().stream()
            .map(entry -> Map.of("alvo", entry.getKey(), "atacantes", entry.getValue().stream()
                .map(m -> ((Map<?, ?>) m).get("atacante")).toList()))
            .collect(Collectors.toList());

        ctx.getContexto().put(destino, gruposList);
        return ResultadoEtapa.concluida(Map.of("quantidade_grupos", gruposList.size()));
    }
}
