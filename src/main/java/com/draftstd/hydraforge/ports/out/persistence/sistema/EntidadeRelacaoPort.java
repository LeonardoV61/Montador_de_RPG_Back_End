package com.draftstd.hydraforge.ports.out.persistence.sistema;

import com.draftstd.hydraforge.domain.model.entidade.EntidadeRelacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntidadeRelacaoPort {

    /**
     * Busca todas as relações onde a entidade pai possui o ID especificado.
     */
    List<EntidadeRelacao> buscarPorEntidadePaiId(UUID entidadePaiId);

    /**
     * Busca uma relação específica combinando o ID do pai e o ID da filha.
     */
    Optional<EntidadeRelacao> buscarPorPaiEFilhaId(UUID entidadePaiId, UUID entidadeFilhaId);

    /**
     * Salva ou atualiza uma entidade de relação.
     */
    EntidadeRelacao salvar(EntidadeRelacao relacao);
}