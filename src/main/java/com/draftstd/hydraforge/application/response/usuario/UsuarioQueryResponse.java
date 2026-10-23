package com.draftstd.hydraforge.application.response.usuario;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioQueryResponse(UUID id, String email, String apelido, String urlImagem, boolean eAdmin, String provider, LocalDateTime criadoEm) {

}