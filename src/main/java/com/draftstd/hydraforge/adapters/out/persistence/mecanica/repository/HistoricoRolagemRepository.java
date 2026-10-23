package com.draftstd.hydraforge.adapters.out.persistence.mecanica.repository;

import com.draftstd.hydraforge.adapters.out.persistence.mecanica.entity.HistoricoRolagemJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HistoricoRolagemRepository extends JpaRepository<HistoricoRolagemJpaEntity, UUID> {

    List<HistoricoRolagemJpaEntity> findBySessaoIdOrderByCriadoEmDesc(UUID sessaoId, Pageable pageable);

    List<HistoricoRolagemJpaEntity> findBySessaoIdAndPersonagemIdOrderByCriadoEmDesc(
            UUID sessaoId, UUID personagemId, Pageable pageable);
}