package com.draftstd.hydraforge.adapters.in.dto.sistema;

import tools.jackson.databind.JsonNode;

public record EventoSistemaResponseDTO(
        Long id,
        Long sistemaId,
        String sistemaNome,
        String nome,
        String descricao,
        JsonNode payloadSchema
) {}