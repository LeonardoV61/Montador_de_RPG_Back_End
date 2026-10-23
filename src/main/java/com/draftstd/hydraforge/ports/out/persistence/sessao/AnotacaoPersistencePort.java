package com.draftstd.hydraforge.ports.out.persistence.sessao;

import com.draftstd.hydraforge.domain.model.sessao.Anotacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnotacaoPersistencePort {
    Anotacao salvar(Anotacao anotacao);
    Optional<Anotacao> buscarPorId(UUID id);
    List<Anotacao> buscarPorSessaoId(UUID sessaoId);
    List<Anotacao> buscarPorCampanhaId(UUID campanhaId);
    void deletarPorId(UUID id);
}