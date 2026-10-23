package com.draftstd.hydraforge.adapters.in.dto.sessao;

import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record CenaUpdateDTO(UUID sessaoId, String titulo, String ambiente, String mapaUrl, Boolean ativa, Integer ordem, String tipo, JsonNode estado) {

}