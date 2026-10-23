package com.draftstd.hydraforge.application.request.importacao;

import java.util.UUID;

public record ExportarPersonagemCommand(UUID personagemId, String formato) {

}