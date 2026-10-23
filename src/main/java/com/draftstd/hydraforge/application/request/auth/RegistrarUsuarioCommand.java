package com.draftstd.hydraforge.application.request.auth;

public record RegistrarUsuarioCommand(String email, String apelido, String senha) {
    
}