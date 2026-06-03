package com.rpgvtt.montador_de_rpg_backend.dto.mecanica;

import tools.jackson.databind.JsonNode;

public record ResolucaoResponseDTO(
        Long id,
        Long sistemaId,
        String sistemaNome,
        String nome,
        String tipo,
        JsonNode parametros
) {}