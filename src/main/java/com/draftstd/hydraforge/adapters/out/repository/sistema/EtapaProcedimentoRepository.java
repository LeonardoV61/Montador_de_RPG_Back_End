package com.draftstd.hydraforge.adapters.out.repository.sistema;

import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtapaProcedimentoRepository extends JpaRepository<EtapaProcedimento, Long> {

    List<EtapaProcedimento> findByProcedimentoIdOrderByOrdem(Long idFilho);
    boolean existsByProcedimentoIdAndOrdem(Long procedimentoId, Integer ordem);
}