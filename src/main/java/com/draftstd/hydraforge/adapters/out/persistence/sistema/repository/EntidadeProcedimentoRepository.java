package com.draftstd.hydraforge.adapters.out.persistence.sistema.repository;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeProcedimentoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EntidadeProcedimentoRepository extends JpaRepository<EntidadeProcedimentoJpaEntity, UUID> {
    List<EntidadeProcedimentoJpaEntity> findByProcedimentoId(UUID procedimentoId);
    List<EntidadeProcedimentoJpaEntity> findByEntidadeInstanciaId(UUID entidadeInstanciaId);
    List<EntidadeProcedimentoJpaEntity> findBySistemaId(UUID sistemaId);
    Optional<EntidadeProcedimentoJpaEntity> findFirstByEntidadeInstanciaIdOrderByOrdemAsc(UUID entidadeInstanciaId);
}