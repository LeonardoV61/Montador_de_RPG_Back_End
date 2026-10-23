package com.draftstd.hydraforge.adapters.out.persistence.sessao.repository;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.AnotacaoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnotacaoRepository extends JpaRepository<AnotacaoJpaEntity, UUID> {
    List<AnotacaoJpaEntity> findBySessao_Id(UUID sessaoId);
    List<AnotacaoJpaEntity> findByCampanha_Id(UUID campanhaId);
}