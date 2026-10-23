package com.draftstd.hydraforge.adapters.in.dto.sessao;

import tools.jackson.databind.JsonNode;
import java.util.List;
import java.util.UUID;

public record CenaResponseDTO(UUID id, UUID sessaoId, JsonNode mapaJson, String urlMapa, Integer ordem, String tipo, JsonNode estado, List<ParticipanteDTO> participantes) {

}