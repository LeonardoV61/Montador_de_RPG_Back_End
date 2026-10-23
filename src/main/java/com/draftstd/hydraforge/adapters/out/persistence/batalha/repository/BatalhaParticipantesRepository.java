package com.draftstd.hydraforge.adapters.out.persistence.batalha.repository;

import com.draftstd.hydraforge.adapters.out.persistence.batalha.entity.BatalhaParticipantesJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BatalhaParticipantesRepository extends JpaRepository<BatalhaParticipantesJpaEntity, UUID> {
}