package com.draftstd.hydraforge.ports.out.persistence.batalha;

import com.draftstd.hydraforge.domain.model.batalha.Batalha;
import java.util.Optional;
import java.util.UUID;

public interface BatalhaOutputPort {
    Optional<Batalha> buscarPorId(UUID id);
    Batalha salvar(Batalha batalha);
    Optional<Batalha> buscarAtivaPorCenaId(UUID cenaId);
    void deletarPorId(UUID id);
}