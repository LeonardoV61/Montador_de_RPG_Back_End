package com.draftstd.hydraforge.adapters.out.persistence.sistema.repository;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeRelacaoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EntidadeRelacaoRepository extends JpaRepository<EntidadeRelacaoJpaEntity, UUID> {
    List<EntidadeRelacaoJpaEntity> findByEntidadePaiId(UUID entidadePaiId);
    Optional<EntidadeRelacaoJpaEntity> findByEntidadePaiIdAndEntidadeFilhaId(UUID entidadePaiId, UUID entidadeFilhaId);
}