package com.draftstd.hydraforge.adapters.out.repository.mecanica;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.model.mecanica.EntidadeProcedimento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntidadeProcedimentoRepository extends JpaRepository<EntidadeProcedimento, Long> {

    Optional<EntidadeProcedimento> findByEntidadeInstancia(EntidadeInstancia entidadeId);
    // Optional<EntidadeProcedimento> findByEntidadeSistemaAndProcessamentoNot();
}
