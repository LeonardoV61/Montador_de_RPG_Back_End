package com.draftstd.hydraforge.adapters.in.dto.importacao;

import tools.jackson.databind.JsonNode;

public record DefinicaoDTO(String alias, String tipo, JsonNode dados) {
    
}
