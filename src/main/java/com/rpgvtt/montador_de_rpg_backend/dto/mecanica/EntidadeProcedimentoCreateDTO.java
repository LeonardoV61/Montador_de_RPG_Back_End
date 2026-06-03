package com.rpgvtt.montador_de_rpg_backend.dto.mecanica;

import jakarta.validation.constraints.NotNull;
import tools.jackson.databind.JsonNode;

public record EntidadeProcedimentoCreateDTO(
        @NotNull(message = "O ID da entidade é obrigatório") Long entidadeId,
        @NotNull(message = "O ID do procedimento é obrigatório") Long procedimentoId,
        String processamento,
        @NotNull(message = "A condição é obrigatória") JsonNode condicao,
        boolean eReativo,
        Integer ordem
) {}