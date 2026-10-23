package com.draftstd.hydraforge.application.request.sessao;

import java.util.UUID;

public record EntrarSessaoCommand(UUID sessaoId, UUID usuarioId, UUID personagemId) {

}