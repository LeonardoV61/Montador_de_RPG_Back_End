package com.rpgvtt.montador_de_rpg_backend.dto.mecanica;

import tools.jackson.databind.JsonNode;

public record EntidadeProcedimentoUpdateDTO(
        String processamento,
        JsonNode condicao,
        Boolean eReativo,
        Integer ordem
) {}