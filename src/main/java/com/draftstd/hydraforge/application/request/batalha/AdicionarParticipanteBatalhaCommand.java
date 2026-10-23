package com.draftstd.hydraforge.application.request.batalha;

import java.util.UUID;

public record AdicionarParticipanteBatalhaCommand(UUID batalhaId, UUID entidadeInstanciaId, String tipo, Integer iniciativa) {
    
}