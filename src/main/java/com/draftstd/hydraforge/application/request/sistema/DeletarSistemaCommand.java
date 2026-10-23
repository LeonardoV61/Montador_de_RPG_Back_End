package com.draftstd.hydraforge.application.request.sistema;

import java.util.UUID;

public record DeletarSistemaCommand(UUID sistemaId, UUID usuarioSolicitanteId) {

}