package com.draftstd.hydraforge.ports.out.sessao;

import com.draftstd.hydraforge.domain.model.sessao.Cena;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObterCenaPort {
    Optional<Cena> buscarPorId(UUID id);
    List<Cena> listarPorSessaoId(UUID sessaoId);
}