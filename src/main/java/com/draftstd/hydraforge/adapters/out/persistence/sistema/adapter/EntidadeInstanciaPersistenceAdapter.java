package com.draftstd.hydraforge.adapters.out.persistence.sistema.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeInstanciaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeSistemaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper.EntidadeInstanciaMapper;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.repository.EntidadeInstanciaRepository;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.repository.EntidadeSistemaRepository;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeInstanciaOutputPort;
import com.draftstd.hydraforge.ports.out.persistence.sistema.SalvarEntidadeInstanciaPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EntidadeInstanciaPersistenceAdapter implements SalvarEntidadeInstanciaPort, EntidadeInstanciaOutputPort {

    private final EntidadeInstanciaRepository entidadeInstanciaRepository;
    private final EntidadeSistemaRepository entidadeSistemaRepository;
    private final EntidadeInstanciaMapper mapper;

    public EntidadeInstanciaPersistenceAdapter(EntidadeInstanciaRepository entidadeInstanciaRepository, 
                                             EntidadeSistemaRepository entidadeSistemaRepository, 
                                             EntidadeInstanciaMapper mapper) {
        this.entidadeInstanciaRepository = entidadeInstanciaRepository;
        this.entidadeSistemaRepository = entidadeSistemaRepository;
        this.mapper = mapper;
    }

    @Override
    public void salvar(EntidadeInstancia instancia) {
        EntidadeSistemaJpaEntity entidadeSistemaJpa = entidadeSistemaRepository.findById(instancia.getEntidadeSistemaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Entidade de Sistema não encontrada: " + instancia.getEntidadeSistemaId()));

        EntidadeInstanciaJpaEntity entity = mapper.toEntity(instancia, entidadeSistemaJpa);
        entidadeInstanciaRepository.save(entity);
    }

    @Override
    public Optional<EntidadeInstancia> buscarPorId(UUID id) {
        return entidadeInstanciaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<EntidadeInstancia> listarPorCampanhaId(UUID campanhaId) {
        return entidadeInstanciaRepository.findAllByCampanhaId(campanhaId)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deletarPorId(UUID id) {
        if (!entidadeInstanciaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Instância de entidade não encontrada para exclusão: " + id);
        }
        entidadeInstanciaRepository.deleteById(id);
    }

    @Override
    public List<EntidadeInstancia> buscarPorIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return entidadeInstanciaRepository.findAllById(ids)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}