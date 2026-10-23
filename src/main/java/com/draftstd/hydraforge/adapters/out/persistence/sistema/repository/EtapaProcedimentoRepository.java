package com.draftstd.hydraforge.adapters.out.persistence.sistema.repository;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EtapaProcedimentoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EtapaProcedimentoRepository extends JpaRepository<EtapaProcedimentoJpaEntity, UUID> {

    List<EtapaProcedimentoJpaEntity> findByProcedimentoIdOrderByOrdem(UUID procedimentoId);
    boolean existsByProcedimentoIdAndOrdem(UUID procedimentoId, Integer ordem);
}