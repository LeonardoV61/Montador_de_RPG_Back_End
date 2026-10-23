package com.draftstd.hydraforge.adapters.out.persistence.sistema.repository;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeInstanciaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntidadeInstanciaRepository extends JpaRepository<EntidadeInstanciaJpaEntity, UUID> {
    List<EntidadeInstanciaJpaEntity> findAllByCampanhaId(UUID campanhaId);
}