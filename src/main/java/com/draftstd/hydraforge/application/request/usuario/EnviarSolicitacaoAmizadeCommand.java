package com.draftstd.hydraforge.application.request.usuario;

import java.util.UUID;

public record EnviarSolicitacaoAmizadeCommand(UUID remetenteId, UUID destinatarioId) {

}