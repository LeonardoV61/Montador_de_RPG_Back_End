package com.draftstd.hydraforge.adapters.out.repository.sistema;

import com.draftstd.hydraforge.domain.model.sistema.EventoSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoSistemaRepository extends JpaRepository<EventoSistema, Long> {

    List<EventoSistema> findBySistemaId(Long sistemaId);

}