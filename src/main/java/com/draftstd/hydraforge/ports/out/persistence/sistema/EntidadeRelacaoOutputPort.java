package com.draftstd.hydraforge.ports.out.persistence.sistema;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeRelacao;

public interface EntidadeRelacaoOutputPort {
    void salvar(EntidadeRelacao relacao);
    Optional<EntidadeRelacao> buscarPorPaiEFilhaId(UUID entidadePaiId, UUID entidadeFilhaId);
    List<EntidadeRelacao> buscarPorEntidadePaiId(UUID entidadePaiId);
    void deletarPorId(UUID id);
}