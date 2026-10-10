package com.draftstd.hydraforge.ports.in.command.sistema;

import java.util.UUID;

public record AtualizarEntidadeSistemaCommand(UUID entidadeSistemaId, String nome, String tipo, String schemaJson) {

}