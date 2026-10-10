package com.draftstd.hydraforge.ports.in.command.personagem;

import java.util.UUID;

public record UsarItemConsumivelCommand(UUID personagemId, UUID itemId, UUID alvoId) {

}