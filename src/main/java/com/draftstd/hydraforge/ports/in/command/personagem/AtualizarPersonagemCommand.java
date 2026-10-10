package com.draftstd.hydraforge.ports.in.command.personagem;

import java.util.UUID;

public record AtualizarPersonagemCommand(UUID personagemId, String nome, String conceito, String dadosJson) {

}