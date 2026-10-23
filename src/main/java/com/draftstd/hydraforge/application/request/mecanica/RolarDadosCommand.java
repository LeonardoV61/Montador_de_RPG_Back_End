package com.draftstd.hydraforge.application.request.mecanica;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record RolarDadosCommand(UUID sessaoId, UUID jogadorId, UUID personagemId, String expressaoDados, List<Integer> dadosSorteados, Integer modificador, String motivo, Map<String, Object> contextoAdicional) {
    public RolarDadosCommand {
        if (dadosSorteados == null) {
            dadosSorteados = List.of();
        }
        if (modificador == null) {
            modificador = 0;
        }
    }
}