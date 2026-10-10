package com.draftstd.hydraforge.adapters.out.projections.persistence.batalha;

import com.draftstd.hydraforge.adapters.out.repository.batalha.BatalhaRepository;
import com.draftstd.hydraforge.domain.model.batalha.Batalha;
import com.draftstd.hydraforge.ports.out.persistence.batalha.BatalhaOutputPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class BatalhaPersistenceAdapter implements BatalhaOutputPort {

    private final BatalhaRepository batalhaRepository;

    public BatalhaPersistenceAdapter(BatalhaRepository batalhaRepository) {
        this.batalhaRepository = batalhaRepository;
    }

    @Override
    public Batalha salvar(Batalha batalha) {
        return batalhaRepository.save(batalha);
    }

    @Override
    public Optional<Batalha> buscarPorId(UUID id) {
        return batalhaRepository.findById(id);
    }

    @Override
    public Optional<Batalha> buscarAtivaPorCenaId(UUID cenaId) {
        return batalhaRepository.findByCenaIdAndStatusAtivo(cenaId);
    }
}