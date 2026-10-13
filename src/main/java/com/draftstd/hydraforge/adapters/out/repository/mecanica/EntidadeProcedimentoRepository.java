package com.draftstd.hydraforge.adapters.out.repository.mecanica;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.domain.model.mecanica.EntidadeProcedimento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EntidadeProcedimentoRepository extends JpaRepository<EntidadeProcedimento, UUID> {
    Optional<EntidadeProcedimento> findByEntidadeInstancia(EntidadeInstancia entidadeId);
}
