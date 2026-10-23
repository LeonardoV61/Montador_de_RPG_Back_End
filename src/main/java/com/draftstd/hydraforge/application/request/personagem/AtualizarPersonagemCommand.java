package com.draftstd.hydraforge.application.request.personagem;

import java.util.Map;
import java.util.UUID;

public record AtualizarPersonagemCommand(UUID personagemId, UUID usuarioId, String nome, String historia, String aparencia, String urlImagem, String notasJogador, Map<String, Object> atributos, UUID sessaoId) {
    
}