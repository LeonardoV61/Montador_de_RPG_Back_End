package com.draftstd.hydraforge.ports.in.command.sistema;

import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record CriarSistemaCommand(String nome, String descricao, String versao, UUID autorId, JsonNode regrasBaseJson) {

}