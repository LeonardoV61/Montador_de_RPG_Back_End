package com.draftstd.hydraforge.adapters.out.persistence.sessao.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.AnotacaoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.mapper.AnotacaoMapper;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.repository.AnotacaoRepository;
import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import com.draftstd.hydraforge.ports.out.persistence.sessao.AnotacaoPersistencePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AnotacaoPersistenceAdapter implements AnotacaoPersistencePort {

    private final AnotacaoRepository anotacaoRepository;
    private final AnotacaoMapper anotacaoMapper;

    public AnotacaoPersistenceAdapter(AnotacaoRepository anotacaoRepository, AnotacaoMapper anotacaoMapper) {
        this.anotacaoRepository = anotacaoRepository;
        this.anotacaoMapper = anotacaoMapper;
    }

    @Override
    public Anotacao salvar(Anotacao anotacao) {
        AnotacaoJpaEntity entity = anotacaoMapper.toEntity(anotacao);
        AnotacaoJpaEntity saved = anotacaoRepository.save(entity);
        return anotacaoMapper.toDomain(saved);
    }

    @Override
    public Optional<Anotacao> buscarPorId(UUID id) {
        return anotacaoRepository.findById(id).map(anotacaoMapper::toDomain);
    }

    @Override
    public List<Anotacao> buscarPorSessaoId(UUID sessaoId) {
        return anotacaoRepository.findBySessao_Id(sessaoId)
                .stream()
                .map(anotacaoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Anotacao> buscarPorCampanhaId(UUID campanhaId) {
        return anotacaoRepository.findByCampanha_Id(campanhaId)
                .stream()
                .map(anotacaoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deletarPorId(UUID id) {
        anotacaoRepository.deleteById(id);
    }
}