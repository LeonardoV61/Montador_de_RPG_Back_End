package com.draftstd.hydraforge.adapters.out.projections.persistence.campanha;

import com.draftstd.hydraforge.adapters.out.repository.campanha.CampanhaRepository;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.ports.out.campanha.ObterCampanhaPort;
import com.draftstd.hydraforge.ports.out.campanha.SalvarCampanhaPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CampanhaPersistenceAdapter implements SalvarCampanhaPort, ObterCampanhaPort {

    private final CampanhaRepository campanhaRepository;

    public CampanhaPersistenceAdapter(CampanhaRepository campanhaRepository) {
        this.campanhaRepository = campanhaRepository;
    }

    @Override
    public Campanha salvar(Campanha campanha) {
        return campanhaRepository.save(campanha);
    }

    @Override
    public Optional<Campanha> buscarPorId(UUID id) {
        return campanhaRepository.findById(id);
    }

    @Override
    public List<Campanha> listarPorUsuarioId(UUID usuarioId) {
        return campanhaRepository.findByUsuarioId(usuarioId);
    }
}