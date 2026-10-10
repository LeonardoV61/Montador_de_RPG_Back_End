package com.draftstd.hydraforge.ports.in.command.importacao;

import java.util.UUID;

public record ExportarPersonagemCommand(UUID personagemId, String formato) {

}