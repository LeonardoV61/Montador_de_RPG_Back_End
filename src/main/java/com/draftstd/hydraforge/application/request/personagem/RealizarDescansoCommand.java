package com.draftstd.hydraforge.application.request.personagem;

import java.util.UUID;

public record RealizarDescansoCommand(UUID personagemId, UUID usuarioId, String tipoDescanso, UUID sessaoId) {
    
}