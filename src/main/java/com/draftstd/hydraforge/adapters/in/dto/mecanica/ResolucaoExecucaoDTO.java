package com.draftstd.hydraforge.adapters.in.dto.mecanica;

import tools.jackson.databind.JsonNode;

public record ResolucaoExecucaoDTO(
        Object roll,
        int targetValue,
        boolean success,
        String motivo,
        JsonNode parametros
) {}
