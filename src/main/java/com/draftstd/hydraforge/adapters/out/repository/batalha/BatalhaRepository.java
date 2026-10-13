package com.draftstd.hydraforge.adapters.out.repository.batalha;

import com.draftstd.hydraforge.domain.model.batalha.Batalha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BatalhaRepository extends JpaRepository<Batalha, UUID> {

    // Se o campo na entidade for 'status', use o nome derivado correto:
    Optional<Batalha> findByCenaIdAndStatus(UUID cenaId, com.draftstd.hydraforge.domain.enums.BatalhaStatus status);
}