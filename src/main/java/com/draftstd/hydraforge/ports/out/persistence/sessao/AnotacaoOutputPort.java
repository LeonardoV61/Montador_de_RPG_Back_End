package com.draftstd.hydraforge.ports.out.persistence.sessao;

import com.draftstd.hydraforge.domain.model.sessao.Anotacao;

import java.util.List;
import java.util.Optional;

public interface AnotacaoOutputPort {
    Anotacao salvar(Anotacao anotacao);
    Optional<Anotacao> buscarPorId(Integer id);
    List<Anotacao> buscarPorSessaoId(Long idSessao);
    List<Anotacao> buscarPorCampanhaId(Long idCampanha);
    void deletarPorId(Integer id);
}