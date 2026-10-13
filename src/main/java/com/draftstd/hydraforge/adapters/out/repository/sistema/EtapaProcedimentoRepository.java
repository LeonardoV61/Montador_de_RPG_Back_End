package com.draftstd.hydraforge.adapters.out.repository.sistema;

import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EtapaProcedimentoRepository extends JpaRepository<EtapaProcedimento, UUID> {

    List<EtapaProcedimento> findByProcedimentoIdOrderByOrdem(UUID idFilho);
    boolean existsByProcedimentoIdAndOrdem(UUID procedimentoId, Integer ordem);
}