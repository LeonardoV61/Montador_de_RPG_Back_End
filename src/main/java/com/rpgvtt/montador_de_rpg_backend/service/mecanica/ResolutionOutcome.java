package com.rpgvtt.montador_de_rpg_backend.service.mecanica;

import tools.jackson.databind.JsonNode;

public record ResolutionOutcome(
        int roll,
        int targetValue,
        boolean success,
        String motivo,
        JsonNode detalhes
) {}
