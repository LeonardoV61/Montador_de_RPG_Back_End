package com.draftstd.hydraforge.adapters.in.dto.sistema;

import tools.jackson.databind.JsonNode;

public record SistemaUpdateDTO(
        String nome,
        String descricao,
        String urlImagem,
        JsonNode configuracao,
        JsonNode schemaAtributos,
        JsonNode schemaEntidades
) {}