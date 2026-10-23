package com.draftstd.hydraforge.adapters.out.persistence.sessao.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.EfeitoAtivoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.mapper.EfeitoAtivoMapper;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.repository.EfeitoAtivoRepository;
import com.draftstd.hydraforge.domain.model.sessao.EfeitoAtivo;
import com.draftstd.hydraforge.ports.out.persistence.sessao.EfeitoAtivoPersistencePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class EfeitoAtivoPersistenceAdapter implements EfeitoAtivoPersistencePort {

    private final EfeitoAtivoRepository efeitoAtivoRepository;
    private final EfeitoAtivoMapper efeitoAtivoMapper;

    public EfeitoAtivoPersistenceAdapter(EfeitoAtivoRepository efeitoAtivoRepository, EfeitoAtivoMapper efeitoAtivoMapper) {
        this.efeitoAtivoRepository = efeitoAtivoRepository;
        this.efeitoAtivoMapper = efeitoAtivoMapper;
    }

    @Override
    public void salvar(UUID sessaoId, EfeitoAtivo efeito) {
        EfeitoAtivoJpaEntity entity = efeitoAtivoMapper.toEntity(efeito, sessaoId);
        efeitoAtivoRepository.save(entity);
    }

    @Override
    public Optional<EfeitoAtivo> buscarPorId(UUID efeitoId) {
        return efeitoAtivoRepository.findById(efeitoId).map(efeitoAtivoMapper::toDomain);
    }

    @Override
    public List<EfeitoAtivo> buscarPorSessaoId(UUID sessaoId) {
        return efeitoAtivoRepository.findBySessao_Id(sessaoId)
                .stream()
                .map(efeitoAtivoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void remover(UUID efeitoId) {
        efeitoAtivoRepository.deleteById(efeitoId);
    }
}