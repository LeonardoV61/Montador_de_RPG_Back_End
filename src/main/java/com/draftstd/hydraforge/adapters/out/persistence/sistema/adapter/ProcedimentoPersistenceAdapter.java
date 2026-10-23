package com.draftstd.hydraforge.adapters.out.persistence.sistema.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.ProcedimentoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.SistemaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper.ProcedimentoMapper;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.repository.ProcedimentoRepository;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.repository.SistemaRepository;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import com.draftstd.hydraforge.ports.out.persistence.sistema.ProcedimentoOutputPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProcedimentoPersistenceAdapter implements ProcedimentoOutputPort {

    private final ProcedimentoRepository procedimentoRepository;
    private final SistemaRepository sistemaRepository;
    private final ProcedimentoMapper procedimentoMapper;

    public ProcedimentoPersistenceAdapter(ProcedimentoRepository procedimentoRepository,
                                          SistemaRepository sistemaRepository,
                                          ProcedimentoMapper procedimentoMapper) {
        this.procedimentoRepository = procedimentoRepository;
        this.sistemaRepository = sistemaRepository;
        this.procedimentoMapper = procedimentoMapper;
    }

    @Override
    public void salvar(Procedimento procedimento) {
        UUID sistemaId = procedimento.getSistemaId();
        if (sistemaId == null) {
            throw new IllegalArgumentException("O ID do sistema não pode ser nulo para o procedimento.");
        }

        SistemaJpaEntity sistemaJpa = sistemaRepository.findById(sistemaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sistema não encontrado: " + sistemaId));

        ProcedimentoJpaEntity entity = procedimentoMapper.toEntity(procedimento, sistemaJpa);
        procedimentoRepository.save(entity);
    }

    @Override
    public Optional<Procedimento> buscarPorId(UUID id) {
        return procedimentoRepository.findById(id).map(procedimentoMapper::toDomain);
    }

    @Override
    public List<Procedimento> listarPorSistema(UUID sistemaId) {
        return procedimentoRepository.findBySistemaId(sistemaId)
                .stream()
                .map(procedimentoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Procedimento> listarTodos() {
        return procedimentoRepository.findAll()
                .stream()
                .map(procedimentoMapper::toDomain)
                .toList();
    }

    @Override
    public void deletarPorId(UUID id) {
        if (!procedimentoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Procedimento não encontrado para exclusão: " + id);
        }
        procedimentoRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(UUID id) {
        return procedimentoRepository.existsById(id);
    }

    @Override
    public Optional<Procedimento> findByTipoAndSistemaId(String tipo, UUID sistemaId) {
        return procedimentoRepository.findByTipoAndSistemaId(tipo, sistemaId)
                .map(procedimentoMapper::toDomain);
    }
}