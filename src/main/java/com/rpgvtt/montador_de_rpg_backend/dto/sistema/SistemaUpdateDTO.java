package com.rpgvtt.montador_de_rpg_backend.dto.sistema;

import tools.jackson.databind.JsonNode;

public record SistemaUpdateDTO(
        String nome,
        String descricao,
        String urlImagem,
        JsonNode configuracao,
        JsonNode schemaAtributos,
        JsonNode schemaEntidades
) {}