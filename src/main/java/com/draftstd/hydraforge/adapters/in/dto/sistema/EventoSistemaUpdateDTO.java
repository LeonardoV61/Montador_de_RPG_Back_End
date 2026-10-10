package com.draftstd.hydraforge.adapters.in.dto.sistema;

import tools.jackson.databind.JsonNode;

public record EventoSistemaUpdateDTO(
        String nome,
        String descricao,
        JsonNode payloadSchema
) {}