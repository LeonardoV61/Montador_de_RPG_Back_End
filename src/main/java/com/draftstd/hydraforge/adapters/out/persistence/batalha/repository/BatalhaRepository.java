package com.draftstd.hydraforge.adapters.out.persistence.batalha.repository;

import com.draftstd.hydraforge.adapters.out.persistence.batalha.entity.BatalhaJpaEntity;
import com.draftstd.hydraforge.domain.enums.BatalhaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BatalhaRepository extends JpaRepository<BatalhaJpaEntity, UUID> {
    Optional<BatalhaJpaEntity> findByCenaIdAndStatus(UUID cenaId, BatalhaStatus status);
}