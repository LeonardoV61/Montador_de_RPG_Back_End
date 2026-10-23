package com.draftstd.hydraforge.adapters.out.persistence.sistema.repository;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.ProcedimentoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcedimentoRepository extends JpaRepository<ProcedimentoJpaEntity, UUID> {
    List<ProcedimentoJpaEntity> findBySistemaId(UUID sistemaId);
    Optional<ProcedimentoJpaEntity> findByTipoAndSistemaId(String tipo, UUID sistemaId);
}