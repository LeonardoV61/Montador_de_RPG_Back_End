package com.draftstd.hydraforge.application.request.sistema;

import java.util.Map;
import java.util.UUID;

public record AtualizarEntidadeSistemaCommand(UUID entidadeSistemaId, String nome, String tipo, Map<String, Object> schema) {

}