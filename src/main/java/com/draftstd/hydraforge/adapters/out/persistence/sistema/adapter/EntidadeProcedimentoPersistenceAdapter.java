package com.draftstd.hydraforge.adapters.out.persistence.sistema.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.*;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper.EntidadeProcedimentoMapper;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.repository.*;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeProcedimento;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EntidadeProcedimentoOutputPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EntidadeProcedimentoPersistenceAdapter implements EntidadeProcedimentoOutputPort {

    private final EntidadeProcedimentoRepository repository;
    private final SistemaRepository sistemaRepository;
    private final EntidadeSistemaRepository entidadeSistemaRepository;
    private final EntidadeInstanciaRepository entidadeInstanciaRepository;
    private final ProcedimentoRepository procedimentoRepository;
    private final EntidadeProcedimentoMapper mapper;

    public EntidadeProcedimentoPersistenceAdapter(EntidadeProcedimentoRepository repository,
                                                  SistemaRepository sistemaRepository,
                                                  EntidadeSistemaRepository entidadeSistemaRepository,
                                                  EntidadeInstanciaRepository entidadeInstanciaRepository,
                                                  ProcedimentoRepository procedimentoRepository,
                                                  EntidadeProcedimentoMapper mapper) {
        this.repository = repository;
        this.sistemaRepository = sistemaRepository;
        this.entidadeSistemaRepository = entidadeSistemaRepository;
        this.entidadeInstanciaRepository = entidadeInstanciaRepository;
        this.procedimentoRepository = procedimentoRepository;
        this.mapper = mapper;
    }

    @Override
    public void salvar(EntidadeProcedimento entidadeProcedimento) {
        EntidadeSistemaJpaEntity entidadeSistema = null;
        if (entidadeProcedimento.getEntidadeSistemaId() != null) {
            entidadeSistema = entidadeSistemaRepository.findById(entidadeProcedimento.getEntidadeSistemaId())
                    .orElse(null);
        }

        EntidadeInstanciaJpaEntity instancia = null;
        if (entidadeProcedimento.getEntidadeInstanciaId() != null) {
            instancia = entidadeInstanciaRepository.findById(entidadeProcedimento.getEntidadeInstanciaId())
                    .orElse(null);
        }

        ProcedimentoJpaEntity procedimento = procedimentoRepository.findById(entidadeProcedimento.getProcedimentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Procedimento não encontrado: " + entidadeProcedimento.getProcedimentoId()));

        SistemaJpaEntity sistema = procedimento.getSistema();
        if (sistema == null && entidadeSistema != null) {
            sistema = entidadeSistema.getSistema();
        }

        if (sistema == null) {
            throw new RecursoNaoEncontradoException("Sistema associado não encontrado para o procedimento/entidade");
        }

        EntidadeProcedimentoJpaEntity entity = mapper.toEntity(entidadeProcedimento, sistema, entidadeSistema, instancia, procedimento);
        repository.save(entity);
    }

    @Override
    public Optional<EntidadeProcedimento> buscarPorId(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<EntidadeProcedimento> buscarPorEntidadeInstanciaId(UUID entidadeInstanciaId) {
        return repository.findFirstByEntidadeInstanciaIdOrderByOrdemAsc(entidadeInstanciaId)
                .map(mapper::toDomain);
    }

    @Override
    public List<EntidadeProcedimento> listarPorEntidadeInstanciaId(UUID entidadeInstanciaId) {
        return repository.findByEntidadeInstanciaId(entidadeInstanciaId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<EntidadeProcedimento> listarPorSistemaId(UUID sistemaId) {
        return repository.findBySistemaId(sistemaId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    /** Fora da porta; mantido para quem ainda chama. */
    public List<EntidadeProcedimento> listarPorProcedimento(UUID procedimentoId) {
        return repository.findByProcedimentoId(procedimentoId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deletarPorId(UUID id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Entidade Procedimento não encontrada para exclusão: " + id);
        }
        repository.deleteById(id);
    }
}