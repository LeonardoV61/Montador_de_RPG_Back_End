package com.draftstd.hydraforge.adapters.in.dto.entidade;

import tools.jackson.databind.JsonNode;

public record EntidadeSistemaResponseDTO(
        Long id,
        Long sistemaId,
        String sistemaNome,
        String tipo,
        String nome,
        String descricao,
        String urlImagem,
        JsonNode atributos,
        JsonNode propriedades
) {}