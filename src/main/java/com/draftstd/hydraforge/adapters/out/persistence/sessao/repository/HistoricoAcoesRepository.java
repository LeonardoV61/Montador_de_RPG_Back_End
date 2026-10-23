package com.draftstd.hydraforge.adapters.out.persistence.sessao.repository;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.HistoricoAcoesJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistoricoAcoesRepository extends JpaRepository<HistoricoAcoesJpaEntity, UUID> {
    List<HistoricoAcoesJpaEntity> findBySessao_Id(UUID sessaoId);
}