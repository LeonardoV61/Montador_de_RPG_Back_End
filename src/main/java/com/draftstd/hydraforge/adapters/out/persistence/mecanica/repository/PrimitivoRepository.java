package com.draftstd.hydraforge.adapters.out.persistence.mecanica.repository;

import com.draftstd.hydraforge.adapters.out.persistence.mecanica.entity.PrimitivoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrimitivoRepository extends JpaRepository<PrimitivoJpaEntity, UUID> {
}