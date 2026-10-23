package com.draftstd.hydraforge.adapters.in.dto.mecanica;

import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record ResolucaoResponseDTO(UUID id, UUID sistemaId, String sistemaNome, String nome, String tipo, JsonNode parametros) {

}