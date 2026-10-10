package com.draftstd.hydraforge.adapters.out.projections.persistence.usuario;

import com.draftstd.hydraforge.adapters.out.repository.usuario.UsuarioRepository;
import com.draftstd.hydraforge.domain.model.usuario.Usuario;
import com.draftstd.hydraforge.ports.out.usuario.ObterUsuarioPort;
import com.draftstd.hydraforge.ports.out.usuario.SalvarUsuarioPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UsuarioPersistenceAdapter implements SalvarUsuarioPort, ObterUsuarioPort {

    private final UsuarioRepository usuarioRepository;

    public UsuarioPersistenceAdapter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}