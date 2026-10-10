package com.draftstd.hydraforge.adapters.in.dto.mecanica;

import tools.jackson.databind.JsonNode;

public record EntidadeProcedimentoResponseDTO(
        Long id,
        Long sistemaId,
        Long entidadeId,
        String entidadeNome,
        Long procedimentoId,
        String procedimentoNome,
        String processamento,
        JsonNode condicao,
        boolean eReativo,
        Integer ordem
) {}