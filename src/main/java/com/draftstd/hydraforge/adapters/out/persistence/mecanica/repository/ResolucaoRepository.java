package com.draftstd.hydraforge.adapters.out.persistence.mecanica.repository;

import com.draftstd.hydraforge.adapters.out.persistence.mecanica.entity.ResolucaoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResolucaoRepository extends JpaRepository<ResolucaoJpaEntity, UUID> {
    List<ResolucaoJpaEntity> findBySistemaId(UUID sistemaId);
}