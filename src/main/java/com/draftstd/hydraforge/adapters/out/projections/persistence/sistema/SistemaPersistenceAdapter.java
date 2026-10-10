package com.draftstd.hydraforge.adapters.out.projections.persistence.sistema;

import com.draftstd.hydraforge.adapters.out.repository.sistema.SistemaRepository;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import com.draftstd.hydraforge.ports.out.sistema.ObterSistemaPort;
import com.draftstd.hydraforge.ports.out.sistema.SalvarSistemaPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class SistemaPersistenceAdapter implements SalvarSistemaPort, ObterSistemaPort {

    private final SistemaRepository sistemaRepository;

    public SistemaPersistenceAdapter(SistemaRepository sistemaRepository) {
        this.sistemaRepository = sistemaRepository;
    }

    @Override
    public Sistema salvar(Sistema sistema) {
        return sistemaRepository.save(sistema);
    }

    @Override
    public Optional<Sistema> buscarPorId(UUID id) {
        return sistemaRepository.findById(id);
    }

    @Override
    public List<Sistema> listarTodos() {
        return sistemaRepository.findAll();
    }
}