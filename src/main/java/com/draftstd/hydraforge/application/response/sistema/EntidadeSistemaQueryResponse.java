package com.draftstd.hydraforge.application.response.sistema;

import java.util.Map;
import java.util.UUID;

public record EntidadeSistemaQueryResponse(UUID id, UUID sistemaId, String nome, String tipo, String descricao, String urlImagem, Map<String, Object> schema, Map<String, Object> propriedades) {

}