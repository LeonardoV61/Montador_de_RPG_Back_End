package com.draftstd.hydraforge.adapters.in.dto.usuario;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponseDTO(UUID id, String nome, String email, String apelido, String urlImagem, boolean eAdmin, LocalDateTime criadoEm) {
    // Construtor de conveniência para chamadas mais simples
    public UsuarioResponseDTO(UUID id, String nome, String email) {
        this(id, nome, email, null, null, false, LocalDateTime.now());
    }
}