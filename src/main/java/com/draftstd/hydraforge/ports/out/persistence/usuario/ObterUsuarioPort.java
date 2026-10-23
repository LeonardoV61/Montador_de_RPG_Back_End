package com.draftstd.hydraforge.ports.out.persistence.usuario;

import com.draftstd.hydraforge.domain.model.usuario.Usuario;
import java.util.Optional;
import java.util.UUID;

public interface ObterUsuarioPort {
    Optional<Usuario> buscarPorId(UUID id);
    Optional<Usuario> buscarPorEmail(String email);
    boolean existePorEmail(String email);
}