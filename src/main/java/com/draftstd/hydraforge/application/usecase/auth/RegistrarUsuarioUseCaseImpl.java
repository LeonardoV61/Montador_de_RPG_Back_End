package com.draftstd.hydraforge.application.usecase.auth;

import com.draftstd.hydraforge.application.request.auth.RegistrarUsuarioCommand;
import com.draftstd.hydraforge.domain.model.usuario.Usuario;
import com.draftstd.hydraforge.ports.in.auth.RegistrarUsuarioUseCase;
import com.draftstd.hydraforge.ports.out.persistence.usuario.ObterUsuarioPort;
import com.draftstd.hydraforge.ports.out.persistence.usuario.SalvarUsuarioPort;
import com.draftstd.hydraforge.ports.out.security.GerarTokenPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RegistrarUsuarioUseCaseImpl implements RegistrarUsuarioUseCase {

    private final ObterUsuarioPort obterUsuarioPort;
    private final SalvarUsuarioPort salvarUsuarioPort;
    private final PasswordEncoder passwordEncoder;
    private final GerarTokenPort gerarTokenPort;

    public RegistrarUsuarioUseCaseImpl(ObterUsuarioPort obterUsuarioPort,
                                       SalvarUsuarioPort salvarUsuarioPort,
                                       PasswordEncoder passwordEncoder,
                                       GerarTokenPort gerarTokenPort) {
        this.obterUsuarioPort = obterUsuarioPort;
        this.salvarUsuarioPort = salvarUsuarioPort;
        this.passwordEncoder = passwordEncoder;
        this.gerarTokenPort = gerarTokenPort;
    }

    @Override
    @Transactional
    public String registrar(RegistrarUsuarioCommand command) {
        if (obterUsuarioPort.existePorEmail(command.email())) {
            throw new IllegalStateException("E-mail já cadastrado.");
        }

        String senhaCriptografada = passwordEncoder.encode(command.senha());

        // Constrói o agregado disparando o evento de domínio correto
        Usuario usuario = new Usuario(
                UUID.randomUUID(),
                command.email(),
                senhaCriptografada,
                command.apelido(),
                null,       // urlImagem
                false,      // eAdmin
                "LOCAL",    // provider
                null        // providerId
        );

        salvarUsuarioPort.salvar(usuario);

        return gerarTokenPort.gerarToken(usuario);
    }
}