package com.draftstd.hydraforge.application.request.sistema;

import java.util.Map;
import java.util.UUID;

public record CriarEntidadeSistemaCommand(UUID sistemaId, String nome, String tipo, Map<String, Object> schema) {

}