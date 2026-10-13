package com.draftstd.hydraforge.ports.in.command.sistema;

import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record CriarEntidadeSistemaCommand(UUID sistemaId, String nome, String tipo, JsonNode schemaJson ) {

}