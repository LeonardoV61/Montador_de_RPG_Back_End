package com.draftstd.hydraforge.ports.out.sessao;

import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObterAnotacaoPort {
    Optional<Anotacao> buscarPorId(UUID id);
    List<Anotacao> listarPorSessaoId(UUID sessaoId);
}