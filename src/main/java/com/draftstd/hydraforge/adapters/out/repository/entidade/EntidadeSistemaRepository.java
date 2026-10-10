package com.draftstd.hydraforge.adapters.out.repository.entidade;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntidadeSistemaRepository extends JpaRepository<EntidadeSistema, Long> {
    List<EntidadeSistema> findBySistemaId(Long sistemaId);

    List<EntidadeSistema> findBySistemaIdAndTipo(Long sistemaId, String tipo);
}