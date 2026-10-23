package com.draftstd.hydraforge.application.request.batalha;

import java.util.UUID;

public record RemoverParticipanteBatalhaCommand(UUID batalhaId, UUID entidadeInstanciaId) {

}