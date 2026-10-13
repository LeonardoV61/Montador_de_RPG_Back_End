package com.draftstd.hydraforge.adapters.out.repository.entidade;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntidadeSistemaRepository extends JpaRepository<EntidadeSistema, UUID> {
    List<EntidadeSistema> findAllBySistemaId(UUID sistemaId);

    List<EntidadeSistema> findBySistemaIdAndTipo(UUID sistemaId, String tipo);
}