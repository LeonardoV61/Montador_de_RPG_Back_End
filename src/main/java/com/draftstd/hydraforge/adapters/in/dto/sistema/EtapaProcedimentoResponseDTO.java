package com.draftstd.hydraforge.adapters.in.dto.sistema;

import tools.jackson.databind.JsonNode;

public record EtapaProcedimentoResponseDTO(
        Long id,
        Long procedimentoId,
        Integer ordem,
        String nome,
        String tipoEtapa,
        JsonNode parametrosEtapa,
        Boolean obrigatorio
) {}