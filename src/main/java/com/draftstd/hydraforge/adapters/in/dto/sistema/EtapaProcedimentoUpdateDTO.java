package com.draftstd.hydraforge.adapters.in.dto.sistema;

import tools.jackson.databind.JsonNode;

public record EtapaProcedimentoUpdateDTO(
        Integer ordem,
        String nome,
        String tipoEtapa,
        JsonNode parametrosEtapa,
        Boolean obrigatorio
) {}