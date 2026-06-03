package com.rpgvtt.montador_de_rpg_backend.dto.entidade;

import tools.jackson.databind.JsonNode;

public record EntidadeRelacaoUpdateDTO(
        Integer quantidade,
        JsonNode customizacoes,
        String origem
) {}