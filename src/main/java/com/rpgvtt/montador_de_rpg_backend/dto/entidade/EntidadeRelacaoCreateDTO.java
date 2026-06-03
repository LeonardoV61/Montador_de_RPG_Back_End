package com.rpgvtt.montador_de_rpg_backend.dto.entidade;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import tools.jackson.databind.JsonNode;

public record EntidadeRelacaoCreateDTO(
        @NotNull(message = "O ID da entidade pai é obrigatório") Long idEntidadePai,
        @NotNull(message = "O ID da entidade filha é obrigatório") Long idEntidadeFilha,
        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero") Integer quantidade,
        @NotNull(message = "As customizações são obrigatórias") JsonNode customizacoes,
        String origem
) {}