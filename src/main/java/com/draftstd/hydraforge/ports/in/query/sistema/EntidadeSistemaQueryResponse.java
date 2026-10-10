package com.draftstd.hydraforge.ports.in.query.sistema;

import java.util.UUID;

public record EntidadeSistemaQueryResponse(UUID id, UUID sistemaId, String nome, String tipo, String schemaJson) {

}