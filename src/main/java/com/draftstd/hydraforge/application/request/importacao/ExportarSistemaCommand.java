package com.draftstd.hydraforge.application.request.importacao;

import java.util.UUID;

public record ExportarSistemaCommand(UUID sistemaId, String formato) {

}