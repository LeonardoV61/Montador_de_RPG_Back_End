package com.draftstd.hydraforge.adapters.out.projections.persistence.sessao;

import com.draftstd.hydraforge.adapters.out.repository.sessao.CenaRepository;
import com.draftstd.hydraforge.domain.model.sessao.Cena;
import com.draftstd.hydraforge.ports.out.sessao.ObterCenaPort;
import com.draftstd.hydraforge.ports.out.sessao.SalvarCenaPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CenaPersistenceAdapter implements SalvarCenaPort, ObterCenaPort {

    private final CenaRepository cenaRepository;

    public CenaPersistenceAdapter(CenaRepository cenaRepository) {
        this.cenaRepository = cenaRepository;
    }

    @Override
    public Cena salvar(Cena cena) {
        return cenaRepository.save(cena);
    }

    @Override
    public Optional<Cena> buscarPorId(UUID id) {
        return cenaRepository.findById(id);
    }

    @Override
    public List<Cena> listarPorSessaoId(UUID sessaoId) {
        return cenaRepository.findAllBySessaoId(sessaoId);
    }
}