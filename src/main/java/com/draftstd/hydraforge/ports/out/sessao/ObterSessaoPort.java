package com.draftstd.hydraforge.ports.out.sessao;

import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObterSessaoPort {
    Optional<Sessao> buscarPorId(UUID id);
    List<Sessao> listarPorCampanhaId(UUID campanhaId);
}
