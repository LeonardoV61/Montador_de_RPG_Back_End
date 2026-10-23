package com.draftstd.hydraforge.adapters.in.dto.usuario;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponseDTO(UUID id, String email, String apelido, String urlImagem, boolean eAdmin, String provider, LocalDateTime criadoEm) {

}