package com.draftstd.hydraforge.ports.in.command.batalha;

import java.util.UUID;

public record AdicionarParticipanteBatalhaCommand(UUID batalhaId, UUID entidadeInstanciaId, String tipo, Integer iniciativa) {
    
}