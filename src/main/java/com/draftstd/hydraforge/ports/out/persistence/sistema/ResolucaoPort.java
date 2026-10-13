package com.draftstd.hydraforge.ports.out.persistence.sistema;

import com.draftstd.hydraforge.domain.model.mecanica.Resolucao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResolucaoPort extends JpaRepository<Resolucao, UUID> {

    Optional<Resolucao> buscarPorId(UUID id);
}