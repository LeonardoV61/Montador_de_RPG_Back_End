package com.draftstd.hydraforge.application.request.personagem;

import java.util.UUID;

public record DeletarPersonagemCommand(UUID personagemId, UUID usuarioId, UUID sessaoId) {

}