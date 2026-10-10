// UsuarioResponseDTO.java
package com.draftstd.hydraforge.dto.usuario;

import java.time.LocalDateTime;

public record UsuarioResponseDTO(
        Long id,
        String email,
        String apelido,
        String urlImagem,
        boolean eAdmin,
        LocalDateTime criadoEm
) {}