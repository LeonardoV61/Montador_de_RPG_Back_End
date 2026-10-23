package com.draftstd.hydraforge.adapters.out.persistence.sistema.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeSistemaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.SistemaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper.EntidadeSistemaMapper;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.repository.EntidadeSistemaRepository;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.repository.SistemaRepository;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeSistema;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeSistemaOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.SalvarEntidadeSistemaPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EntidadeSistemaPersistenceAdapter implements SalvarEntidadeSistemaPort, EntidadeSistemaOutputPort {

    private final EntidadeSistemaRepository entidadeSistemaRepository;
    private final SistemaRepository sistemaRepository;
    private final EntidadeSistemaMapper mapper;

    public EntidadeSistemaPersistenceAdapter(EntidadeSistemaRepository entidadeSistemaRepository,
                                             SistemaRepository sistemaRepository,
                                             EntidadeSistemaMapper mapper) {
        this.entidadeSistemaRepository = entidadeSistemaRepository;
        this.sistemaRepository = sistemaRepository;
        this.mapper = mapper;
    }

    @Override
    public void salvar(EntidadeSistema entidadeSistema) {
        UUID sistemaId = entidadeSistema.getSistemaId();
        if (sistemaId == null) {
            throw new IllegalArgumentException("O ID do sistema não pode ser nulo na entidade de sistema.");
        }

        SistemaJpaEntity sistemaJpa = sistemaRepository.findById(sistemaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sistema não encontrado: " + sistemaId));

        EntidadeSistemaJpaEntity entity = mapper.toEntity(entidadeSistema, sistemaJpa);
        entidadeSistemaRepository.save(entity);
    }

    @Override
    public Optional<EntidadeSistema> buscarPorId(UUID id) {
        return entidadeSistemaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<EntidadeSistema> listarPorSistemaId(UUID sistemaId) {
        return entidadeSistemaRepository.findAllBySistemaId(sistemaId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}