package com.draftstd.hydraforge.application.request.usuario;

import com.draftstd.hydraforge.domain.enums.StatusAmizade;
import java.util.UUID;

public record ResponderSolicitacaoAmizadeCommand(UUID remetenteId, UUID destinatarioId, StatusAmizade status) {

}