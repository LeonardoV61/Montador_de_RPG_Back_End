package com.draftstd.hydraforge.adapters.in.dto.personagem;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record PersonagemResponseDTO(UUID id, UUID usuarioId, UUID campanhaId, UUID instanciaId, String nome, String historia, boolean ativo, String aparencia, String urlImagem, String notasJogador, Map<String, Object> atributos, LocalDateTime criadoEm) {

}