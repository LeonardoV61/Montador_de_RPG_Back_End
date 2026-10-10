package com.draftstd.hydraforge.adapters.in.dto.mecanica;

import tools.jackson.databind.JsonNode;

public record ResolucaoResponseDTO(
        Long id,
        Long sistemaId,
        String sistemaNome,
        String nome,
        String tipo,
        JsonNode parametros
) {}