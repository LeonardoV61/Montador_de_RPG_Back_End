package com.draftstd.hydraforge.adapters.in.dto.sistema.entidade;

import java.util.Map;
import java.util.UUID;

public record EntidadeSistemaResponseDTO(UUID id, UUID sistemaId, String sistemaNome, String tipo, String nome, String descricao, String urlImagem, Map<String, Object> atributos, Map<String, Object> propriedades) {

}