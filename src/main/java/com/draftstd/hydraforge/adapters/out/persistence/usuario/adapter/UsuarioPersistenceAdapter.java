package com.draftstd.hydraforge.adapters.out.persistence.usuario.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.usuario.entity.UsuarioJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.usuario.mapper.UsuarioMapper;
import com.draftstd.hydraforge.adapters.out.persistence.usuario.repository.UsuarioRepository;
import com.draftstd.hydraforge.domain.model.usuario.Usuario;
import com.draftstd.hydraforge.ports.out.persistence.usuario.ObterUsuarioPort;
import com.draftstd.hydraforge.ports.out.persistence.usuario.SalvarUsuarioPort;
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
    public void salvar(Usuario usuario) {
        UsuarioJpaEntity entity = UsuarioMapper.toEntity(usuario);
        usuarioRepository.save(entity);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}