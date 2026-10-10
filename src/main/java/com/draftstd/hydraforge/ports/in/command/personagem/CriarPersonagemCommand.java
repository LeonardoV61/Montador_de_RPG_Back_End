package com.draftstd.hydraforge.ports.in.command.personagem;

import java.util.UUID;

public record CriarPersonagemCommand(UUID usuarioId, UUID sistemaId, UUID campanhaId, String nome, String conceito, String dadosJson) {

}