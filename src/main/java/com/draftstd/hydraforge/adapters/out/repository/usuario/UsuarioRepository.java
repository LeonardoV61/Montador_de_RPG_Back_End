package com.draftstd.hydraforge.adapters.out.repository.usuario;

import com.draftstd.hydraforge.domain.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> { 
    Optional<Usuario> findByEmail(String email); 
    boolean existsByEmail(String email); 
}