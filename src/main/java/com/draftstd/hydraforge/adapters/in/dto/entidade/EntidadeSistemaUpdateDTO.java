package com.draftstd.hydraforge.adapters.in.dto.entidade;

import tools.jackson.databind.JsonNode;

public record EntidadeSistemaUpdateDTO(
        String nome,
        String descricao,
        String urlImagem,
        JsonNode atributos,
        JsonNode propriedades
        // tipo fora do update porque mudaria a semântica inteira
) {}