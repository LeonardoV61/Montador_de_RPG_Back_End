package com.draftstd.hydraforge.adapters.out.persistence.personagem.repository;

import com.draftstd.hydraforge.adapters.out.persistence.personagem.entity.PersonagemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonagemRepository extends JpaRepository<PersonagemJpaEntity, UUID> {
    List<PersonagemJpaEntity> findAllByUsuarioId(UUID usuarioId);
    List<PersonagemJpaEntity> findAllByCampanhaId(UUID campanhaId);
    List<PersonagemJpaEntity> findByCampanhaIdAndAtivo(UUID campanhaId, boolean ativo);
    Optional<PersonagemJpaEntity> findByIdAndUsuarioId(UUID id, UUID usuarioId);
    Optional<PersonagemJpaEntity> findByCampanhaIdAndUsuarioIdAndAtivo(UUID campanhaId, UUID usuarioId, boolean ativo);
}