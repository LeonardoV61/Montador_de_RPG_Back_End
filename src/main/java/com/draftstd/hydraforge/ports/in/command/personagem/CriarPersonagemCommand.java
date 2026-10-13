package com.draftstd.hydraforge.ports.in.command.personagem;

import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record CriarPersonagemCommand(UUID usuarioId, UUID sistemaId, UUID campanhaId, String nome, String conceito, JsonNode dadosJson) {

}