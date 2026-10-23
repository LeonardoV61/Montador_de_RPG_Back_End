package com.draftstd.hydraforge.adapters.out.persistence.usuario.repository;

import com.draftstd.hydraforge.adapters.out.persistence.usuario.entity.UsuarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioJpaEntity, UUID> {
    Optional<UsuarioJpaEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}