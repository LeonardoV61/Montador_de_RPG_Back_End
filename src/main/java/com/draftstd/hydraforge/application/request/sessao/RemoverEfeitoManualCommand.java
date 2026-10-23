package com.draftstd.hydraforge.application.request.sessao;

import java.util.UUID;

public record RemoverEfeitoManualCommand(UUID sessaoId, UUID efeitoId) {
    
}