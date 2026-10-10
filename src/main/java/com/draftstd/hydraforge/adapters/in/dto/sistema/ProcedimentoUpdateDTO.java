package com.draftstd.hydraforge.adapters.in.dto.sistema;

import tools.jackson.databind.JsonNode;

public record ProcedimentoUpdateDTO(
        String nome,
        String descricao,
        String tipo,
        JsonNode configsGeral
) {}