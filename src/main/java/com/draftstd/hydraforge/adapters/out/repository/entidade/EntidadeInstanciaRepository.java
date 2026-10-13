package com.draftstd.hydraforge.adapters.out.repository.entidade;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeInstancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntidadeInstanciaRepository extends JpaRepository<EntidadeInstancia, UUID> {
    List<EntidadeInstancia> findAllByCampanhaId(UUID donoId);
}