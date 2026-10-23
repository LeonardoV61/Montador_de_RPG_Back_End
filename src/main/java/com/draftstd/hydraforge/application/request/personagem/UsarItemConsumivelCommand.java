package com.draftstd.hydraforge.application.request.personagem;

import java.util.UUID;

public record UsarItemConsumivelCommand(UUID personagemId, UUID usuarioId, UUID itemId, UUID alvoId, UUID sessaoId) {

}