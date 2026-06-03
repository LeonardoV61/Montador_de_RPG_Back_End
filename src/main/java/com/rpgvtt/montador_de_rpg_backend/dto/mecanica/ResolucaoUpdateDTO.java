package com.rpgvtt.montador_de_rpg_backend.dto.mecanica;

import tools.jackson.databind.JsonNode;

public record ResolucaoUpdateDTO(
        String nome,
        JsonNode parametros
) {}