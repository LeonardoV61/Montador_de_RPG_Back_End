package com.rpgvtt.montador_de_rpg_backend.dto.sistema;

import tools.jackson.databind.JsonNode;

public record EtapaProcedimentoUpdateDTO(
        Integer ordem,
        String nome,
        String tipoEtapa,
        JsonNode parametrosEtapa,
        Boolean obrigatorio
) {}