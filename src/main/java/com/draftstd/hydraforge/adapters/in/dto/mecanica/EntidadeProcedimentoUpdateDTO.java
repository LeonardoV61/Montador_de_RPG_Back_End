package com.draftstd.hydraforge.adapters.in.dto.mecanica;

import tools.jackson.databind.JsonNode;

public record EntidadeProcedimentoUpdateDTO(
        String processamento,
        JsonNode condicao,
        Boolean eReativo,
        Integer ordem
) {}