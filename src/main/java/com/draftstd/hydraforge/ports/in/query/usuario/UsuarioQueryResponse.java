package com.draftstd.hydraforge.ports.in.query.usuario;

import java.util.UUID;

public record UsuarioQueryResponse(UUID id, String nome, String email) {

}