package com.draftstd.hydraforge.adapters.in.dto.sistema;

import java.util.Map;
import java.util.UUID;

public record EventoSistemaResponseDTO(UUID id, UUID sistemaId, String sistemaNome, String nome, String descricao, Map<String, Object> payloadSchema) {

}