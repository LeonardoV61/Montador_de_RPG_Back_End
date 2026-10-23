package com.draftstd.hydraforge.adapters.out.persistence.sessao.repository;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.MensagemLogJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MensagemLogRepository extends JpaRepository<MensagemLogJpaEntity, UUID> {
    List<MensagemLogJpaEntity> findBySessao_Id(UUID sessaoId);
}