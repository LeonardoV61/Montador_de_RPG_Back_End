package com.draftstd.hydraforge.adapters.in.dto.entidade;

import tools.jackson.databind.JsonNode;

public record EntidadeRelacaoUpdateDTO(
        Integer quantidade,
        JsonNode customizacoes,
        String origem
) {}