package com.draftstd.hydraforge.adapters.in.dto.entidade;

import tools.jackson.databind.JsonNode;

public record EntidadeRelacaoResponseDTO(
        Long idEntidadePai,
        String nomeEntidadePai,
        Long idEntidadeFilha,
        String nomeEntidadeFilha,
        Integer quantidade,
        JsonNode customizacoes,
        String origem
) {}