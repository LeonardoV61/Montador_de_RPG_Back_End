package com.draftstd.hydraforge.adapters.out.persistence.sistema.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EtapaProcedimentoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.ProcedimentoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper.EtapaProcedimentoMapper;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.repository.EtapaProcedimentoRepository;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.repository.ProcedimentoRepository;
import com.draftstd.hydraforge.domain.exceptions.RecursoNaoEncontradoException;
import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import com.draftstd.hydraforge.ports.out.persistence.sistema.EtapaProcedimentoOutputPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EtapaProcedimentoPersistenceAdapter implements EtapaProcedimentoOutputPort {

    private final EtapaProcedimentoRepository etapaRepository;
    private final ProcedimentoRepository procedimentoRepository;
    private final EtapaProcedimentoMapper mapper;

    public EtapaProcedimentoPersistenceAdapter(EtapaProcedimentoRepository etapaRepository,
                                                ProcedimentoRepository procedimentoRepository,
                                                EtapaProcedimentoMapper mapper) {
        this.etapaRepository = etapaRepository;
        this.procedimentoRepository = procedimentoRepository;
        this.mapper = mapper;
    }

    @Override
    public void salvar(EtapaProcedimento etapa) {
        ProcedimentoJpaEntity procedimentoJpa = procedimentoRepository.findById(etapa.getProcedimentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Procedimento não encontrado: " + etapa.getProcedimentoId()));

        EtapaProcedimentoJpaEntity entity = mapper.toEntity(etapa, procedimentoJpa);
        etapaRepository.save(entity);
    }

    @Override
    public Optional<EtapaProcedimento> buscarPorId(UUID id) {
        return etapaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<EtapaProcedimento> buscarPorProcedimentoIdOrdenado(UUID procedimentoId) {
        return etapaRepository.findByProcedimentoIdOrderByOrdem(procedimentoId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorProcedimentoEOrdem(UUID procedimentoId, Integer ordem) {
        return etapaRepository.existsByProcedimentoIdAndOrdem(procedimentoId, ordem);
    }

    @Override
    public void deletarPorId(UUID id) {
        if (!etapaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Etapa de procedimento não encontrada para exclusão: " + id);
        }
        etapaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(UUID id) {
        return etapaRepository.existsById(id);
    }
}