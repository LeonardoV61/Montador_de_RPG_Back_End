package com.draftstd.hydraforge.ports.out.persistence.sessao;

import com.draftstd.hydraforge.domain.model.sessao.HistoricoAcoes;

import java.util.List;
import java.util.UUID;

public interface HistoricoAcoesPersistencePort {

    void salvar(UUID sessaoId, HistoricoAcoes historicoAcoes);

    List<HistoricoAcoes> listarPorSessaoId(UUID sessaoId);
}