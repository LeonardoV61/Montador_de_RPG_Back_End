package com.draftstd.hydraforge.adapters.out.projections.persistence.entidade;

import com.draftstd.hydraforge.adapters.out.repository.entidade.EntidadeSistemaRepository;
import com.draftstd.hydraforge.domain.model.entidade.EntidadeSistema;
import com.draftstd.hydraforge.ports.out.persistence.entidade.EntidadeSistemaOutputPort;
import com.draftstd.hydraforge.ports.out.sistema.SalvarEntidadeSistemaPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EntidadeSistemaPersistenceAdapter implements SalvarEntidadeSistemaPort, EntidadeSistemaOutputPort {

    private final EntidadeSistemaRepository entidadeSistemaRepository;

    public EntidadeSistemaPersistenceAdapter(EntidadeSistemaRepository entidadeSistemaRepository) {
        this.entidadeSistemaRepository = entidadeSistemaRepository;
    }

    @Override
    public EntidadeSistema salvar(EntidadeSistema entidadeSistema) {
        return entidadeSistemaRepository.save(entidadeSistema);
    }

    @Override
    public Optional<EntidadeSistema> buscarPorId(UUID id) {
        return entidadeSistemaRepository.findById(id);
    }

    @Override
    public List<EntidadeSistema> listarPorSistemaId(UUID sistemaId) {
        return entidadeSistemaRepository.findAllBySistemaId(sistemaId);
    }
}