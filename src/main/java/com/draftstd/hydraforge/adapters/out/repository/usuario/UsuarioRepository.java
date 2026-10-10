package com.draftstd.hydraforge.adapters.out.repository.usuario;

import com.draftstd.hydraforge.domain.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
// Método necessário para verificar se o utilizador já está cadastrado
    Optional<Usuario> findByEmail(String email);
}