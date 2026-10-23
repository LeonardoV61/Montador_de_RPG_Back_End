package com.draftstd.hydraforge.ports.out.persistence.sessao;

import com.draftstd.hydraforge.domain.model.sessao.EfeitoAtivo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EfeitoAtivoPersistencePort {
    void salvar(UUID sessaoId, EfeitoAtivo efeito);
    Optional<EfeitoAtivo> buscarPorId(UUID efeitoId);
    List<EfeitoAtivo> buscarPorSessaoId(UUID sessaoId);
    void remover(UUID efeitoId);
}