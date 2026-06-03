package com.rpgvtt.montador_de_rpg_backend.dto.entidade;

import tools.jackson.databind.JsonNode;

public record EntidadeSistemaUpdateDTO(
        String nome,
        String descricao,
        String urlImagem,
        JsonNode atributos,
        JsonNode propriedades
        // tipo fora do update porque mudaria a semântica inteira
) {}