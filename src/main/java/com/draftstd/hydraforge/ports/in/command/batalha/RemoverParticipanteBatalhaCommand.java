package com.draftstd.hydraforge.ports.in.command.batalha;

import java.util.UUID;

public record RemoverParticipanteBatalhaCommand(UUID batalhaId, UUID entidadeInstanciaId) {

}