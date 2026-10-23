package com.draftstd.hydraforge.adapters.out.persistence.sessao.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.HistoricoAcoesJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.mapper.HistoricoAcoesMapper;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.repository.HistoricoAcoesRepository;
import com.draftstd.hydraforge.domain.model.sessao.HistoricoAcoes;
import com.draftstd.hydraforge.ports.out.persistence.sessao.HistoricoAcoesPersistencePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class HistoricoAcoesPersistenceAdapter implements HistoricoAcoesPersistencePort {

    private final HistoricoAcoesRepository historicoAcoesRepository;
    private final HistoricoAcoesMapper historicoAcoesMapper;

    public HistoricoAcoesPersistenceAdapter(HistoricoAcoesRepository historicoAcoesRepository, HistoricoAcoesMapper historicoAcoesMapper) {
        this.historicoAcoesRepository = historicoAcoesRepository;
        this.historicoAcoesMapper = historicoAcoesMapper;
    }

    @Override
    public void salvar(UUID sessaoId, HistoricoAcoes historicoAcoes) {
        HistoricoAcoesJpaEntity entity = historicoAcoesMapper.toEntity(historicoAcoes, sessaoId);
        historicoAcoesRepository.save(entity);
    }

    @Override
    public List<HistoricoAcoes> listarPorSessaoId(UUID sessaoId) {
        return historicoAcoesRepository.findBySessao_Id(sessaoId)
                .stream()
                .map(historicoAcoesMapper::toDomain)
                .collect(Collectors.toList());
    }
}