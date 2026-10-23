package com.draftstd.hydraforge.ports.in.auth;

import com.draftstd.hydraforge.application.request.auth.LoginCommand;

public interface AutenticarUsuarioUseCase {
    String autenticar(LoginCommand command);
}