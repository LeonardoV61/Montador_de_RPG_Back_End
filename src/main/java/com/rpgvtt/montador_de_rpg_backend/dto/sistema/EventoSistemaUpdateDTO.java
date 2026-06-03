package com.rpgvtt.montador_de_rpg_backend.dto.sistema;

import tools.jackson.databind.JsonNode;

public record EventoSistemaUpdateDTO(
        String nome,
        String descricao,
        JsonNode payloadSchema
) {}