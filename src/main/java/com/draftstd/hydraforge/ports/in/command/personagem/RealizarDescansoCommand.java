package com.draftstd.hydraforge.ports.in.command.personagem;

import java.util.UUID;

public record RealizarDescansoCommand(UUID personagemId, String tipoDescanso) {

}