package com.draftstd.hydraforge.ports.in.command.personagem;

import java.util.UUID;

public record DeletarPersonagemCommand(UUID personagemId, UUID usuarioId) {

}