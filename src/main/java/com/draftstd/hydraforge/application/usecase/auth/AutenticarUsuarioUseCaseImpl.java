package com.draftstd.hydraforge.application.usecase.auth;

import com.draftstd.hydraforge.application.request.auth.LoginCommand;
import com.draftstd.hydraforge.domain.model.usuario.Usuario;
import com.draftstd.hydraforge.ports.in.auth.AutenticarUsuarioUseCase;
import com.draftstd.hydraforge.ports.out.persistence.usuario.ObterUsuarioPort;
import com.draftstd.hydraforge.ports.out.security.GerarTokenPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticarUsuarioUseCaseImpl implements AutenticarUsuarioUseCase {

    private final ObterUsuarioPort obterUsuarioPort;
    private final PasswordEncoder passwordEncoder;
    private final GerarTokenPort gerarTokenPort;

    public AutenticarUsuarioUseCaseImpl(ObterUsuarioPort obterUsuarioPort,
                                        PasswordEncoder passwordEncoder,
                                        GerarTokenPort gerarTokenPort) {
        this.obterUsuarioPort = obterUsuarioPort;
        this.passwordEncoder = passwordEncoder;
        this.gerarTokenPort = gerarTokenPort;
    }

    @Override
    public String autenticar(LoginCommand command) {
        Usuario usuario = obterUsuarioPort.buscarPorEmail(command.email())
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));

        if (!passwordEncoder.matches(command.senha(), usuario.getSenha())) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }

        return gerarTokenPort.gerarToken(usuario);
    }
}