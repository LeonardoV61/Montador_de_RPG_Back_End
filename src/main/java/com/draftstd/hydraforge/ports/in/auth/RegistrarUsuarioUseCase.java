package com.draftstd.hydraforge.ports.in.auth;

import com.draftstd.hydraforge.application.request.auth.RegistrarUsuarioCommand;

public interface RegistrarUsuarioUseCase {
    String registrar(RegistrarUsuarioCommand command);
}