package com.draftstd.hydraforge.ports.in.command.personagem;

import tools.jackson.databind.JsonNode;
import java.util.UUID;

public record AtualizarPersonagemCommand(UUID personagemId, String nome, String conceito, JsonNode dadosJson) {

}