package com.draftstd.hydraforge.ports.out.persistence.sistema;

import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EtapaProcedimentoOutputPort {
    EtapaProcedimento salvar(EtapaProcedimento etapa);
    Optional<EtapaProcedimento> buscarPorId(UUID id);
    List<EtapaProcedimento> buscarPorProcedimentoIdOrdenado(UUID procedimentoId);
    boolean existePorProcedimentoEOrdem(UUID procedimentoId, Integer ordem);
    void deletarPorId(UUID id);
    boolean existePorId(UUID id);
}