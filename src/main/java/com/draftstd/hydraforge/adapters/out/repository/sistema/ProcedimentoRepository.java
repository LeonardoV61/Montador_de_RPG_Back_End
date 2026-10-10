package com.draftstd.hydraforge.adapters.out.repository.sistema;

import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {

    List<Procedimento> findBySistemaId(Long sistemaId);
    Optional<Procedimento> findByTipoAndSistemaId(String tipo, Long sistemaId);

}