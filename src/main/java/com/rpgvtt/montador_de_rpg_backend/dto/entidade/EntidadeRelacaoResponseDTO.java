package com.rpgvtt.montador_de_rpg_backend.dto.entidade;

import tools.jackson.databind.JsonNode;

public record EntidadeRelacaoResponseDTO(
        Long idEntidadePai,
        String nomeEntidadePai,
        Long idEntidadeFilha,
        String nomeEntidadeFilha,
        Integer quantidade,
        JsonNode customizacoes,
        String origem
) {}