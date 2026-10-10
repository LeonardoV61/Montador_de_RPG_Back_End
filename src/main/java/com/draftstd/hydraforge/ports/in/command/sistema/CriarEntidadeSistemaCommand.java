package com.draftstd.hydraforge.ports.in.command.sistema;

import java.util.UUID;

public record CriarEntidadeSistemaCommand(UUID sistemaId, String nome, String tipo, String schemaJson) {

}