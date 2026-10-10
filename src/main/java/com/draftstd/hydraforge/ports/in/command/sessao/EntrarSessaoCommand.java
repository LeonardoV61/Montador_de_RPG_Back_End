package com.draftstd.hydraforge.ports.in.command.sessao;

import java.util.UUID;

public record EntrarSessaoCommand(UUID sessaoId, UUID usuarioId, UUID personagemId) {

}