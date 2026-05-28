package com.rpgvtt.montador_de_rpg_backend.domain.engine.primitivos;

import com.rpgvtt.montador_de_rpg_backend.domain.engine.utils.ContextoDinamico;
import com.rpgvtt.montador_de_rpg_backend.domain.engine.components.InterpretadorJson;
import com.rpgvtt.montador_de_rpg_backend.domain.engine.utils.ResultadoExpressao;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;
import java.util.function.Function;

public class AlvoResolver {

    private final EstadoSessao estadoSessao;
    private final InterpretadorJson interpretador;
    private final Function<Long, ContextoDinamico> contextoFactory;

    public AlvoResolver(EstadoSessao estadoSessao,
                        InterpretadorJson interpretador,
                        Function<Long, ContextoDinamico> contextoFactory) {
        this.estadoSessao = estadoSessao;
        this.interpretador = interpretador;
        this.contextoFactory = contextoFactory;
    }

    /**
     * Resolve um nó JSON de alvo para uma lista de IDs de entidade.
     * @param alvoNode nó JSON da expressão de alvo (ex: {"tipo":"alvo_self"})
     * @param selfId ID da entidade que está executando a ação (pode ser null)
     * @return lista de IDs (nunca nula)
     */
    public List<Long> resolver(JsonNode alvoNode, Long selfId) {
        String tipo = alvoNode.get("tipo").asText();
        switch (tipo) {
            case "alvo"          : return resolverAlvoUnico(alvoNode, selfId);
            case "alvo_self"     : return selfId != null ? List.of(selfId) : Collections.emptyList();
            case "alvo_multiplo" : return resolverAlvoMultiplo(alvoNode);
            default: throw new IllegalArgumentException("Tipo de alvo desconhecido: " + tipo);
        }
    }

    private List<Long> resolverAlvoUnico(JsonNode node, Long selfId) {
        JsonNode idNode = node.get("id");
        if (idNode == null) throw new IllegalArgumentException("Alvo requer 'id'");
        Long id;
        if (idNode.isObject() && idNode.has("tipo")) {
            // Expressão que retorna um ID
            ContextoDinamico ctx = contextoFactory.apply(selfId);
            ResultadoExpressao res = interpretador.interpretar(idNode, ctx);
            id = ((Number) res.getValor()).longValue();
        } else if (idNode.isNumber()) {
            id = idNode.asLong();
        } else if (idNode.isTextual()) {
            String texto = idNode.asText();
            if ("self".equals(texto)) {
                id = selfId;
            } else {
                id = Long.parseLong(texto);
            }
        } else {
            throw new IllegalArgumentException("Formato de ID de alvo inválido");
        }
        return List.of(id);
    }

    private List<Long> resolverAlvoMultiplo(JsonNode node) {
        JsonNode filtro = node.get("filtro");
        if (filtro == null) throw new IllegalArgumentException("alvo_multiplo precisa de 'filtro'");
        List<Long> todosIds = new ArrayList<>(estadoSessao.getEntidades().keySet());
        List<Long> selecionados = new ArrayList<>();
        for (Long id : todosIds) {
            ContextoDinamico ctx = contextoFactory.apply(id);
            ResultadoExpressao res = interpretador.interpretar(filtro, ctx);
            if (res.comoBooleano()) {
                selecionados.add(id);
            }
        }
        return selecionados;
    }
}