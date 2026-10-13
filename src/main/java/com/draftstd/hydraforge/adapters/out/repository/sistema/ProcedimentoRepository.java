package com.draftstd.hydraforge.adapters.out.repository.sistema;

import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, UUID> {

    List<Procedimento> findBySistemaId(UUID sistemaId);
    Optional<Procedimento> findByTipoAndSistemaId(String tipo, UUID sistemaId);

}