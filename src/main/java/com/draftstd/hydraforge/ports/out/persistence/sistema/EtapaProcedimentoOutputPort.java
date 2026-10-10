package com.draftstd.hydraforge.ports.out.persistence.sistema;

import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import java.util.List;
import java.util.Optional;

public interface EtapaProcedimentoOutputPort {
    EtapaProcedimento salvar(EtapaProcedimento etapa);
    Optional<EtapaProcedimento> buscarPorId(Long id);
    List<EtapaProcedimento> buscarPorProcedimentoIdOrdenado(Long procedimentoId);
    boolean existePorProcedimentoEOrdem(Long procedimentoId, Integer ordem);
    void deletarPorId(Long id);
    boolean existePorId(Long id);
}