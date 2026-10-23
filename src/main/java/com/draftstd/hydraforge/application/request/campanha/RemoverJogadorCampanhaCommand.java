package com.draftstd.hydraforge.application.request.campanha;

import java.util.UUID;

public record RemoverJogadorCampanhaCommand(UUID campanhaId, UUID usuarioId) {

}