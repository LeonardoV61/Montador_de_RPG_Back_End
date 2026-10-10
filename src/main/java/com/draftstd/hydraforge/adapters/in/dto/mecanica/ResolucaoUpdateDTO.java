package com.draftstd.hydraforge.adapters.in.dto.mecanica;

import tools.jackson.databind.JsonNode;

public record ResolucaoUpdateDTO(
        String nome,
        JsonNode parametros
) {}