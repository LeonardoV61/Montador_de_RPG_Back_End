package com.draftstd.hydraforge.adapters.in.dto.sessao;

import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record AnotacaoResponseDTO(UUID id, UUID usuarioId, UUID campanhaId, UUID sessaoId, String titulo, String conteudo, String categoria, JsonNode anotacaoComplexa, Boolean ePrivado) {

}