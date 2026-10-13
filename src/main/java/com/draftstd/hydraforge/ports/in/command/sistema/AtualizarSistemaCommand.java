package com.draftstd.hydraforge.ports.in.command.sistema;

import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record AtualizarSistemaCommand(UUID sistemaId, String nome, String descricao, String versao, JsonNode regrasBaseJson) {

}