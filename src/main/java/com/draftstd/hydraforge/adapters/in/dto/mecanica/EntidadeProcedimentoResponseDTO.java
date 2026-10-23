package com.draftstd.hydraforge.adapters.in.dto.mecanica;

import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record EntidadeProcedimentoResponseDTO(UUID id, UUID sistemaId, UUID entidadeId, String entidadeNome, UUID procedimentoId, String procedimentoNome, String processamento, JsonNode condicao, boolean eReativo, Integer ordem) {

}