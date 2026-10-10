package com.draftstd.hydraforge.ports.in.command.campanha;

import java.util.UUID;

public record RemoverJogadorCampanhaCommand(UUID campanhaId, UUID usuarioId) {

}