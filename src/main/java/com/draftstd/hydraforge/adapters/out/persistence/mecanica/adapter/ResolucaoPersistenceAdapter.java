package com.draftstd.hydraforge.adapters.out.persistence.mecanica.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.mecanica.entity.ResolucaoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.mecanica.mapper.ResolucaoMapper;
import com.draftstd.hydraforge.adapters.out.persistence.mecanica.repository.ResolucaoRepository;
import com.draftstd.hydraforge.domain.model.mecanica.Resolucao;
import com.draftstd.hydraforge.ports.out.persistence.mecanica.ResolucaoOutputPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ResolucaoPersistenceAdapter implements ResolucaoOutputPort {

    private final ResolucaoRepository resolucaoRepository;
    private final ResolucaoMapper resolucaoMapper;

    public ResolucaoPersistenceAdapter(ResolucaoRepository resolucaoRepository, ResolucaoMapper resolucaoMapper) {
        this.resolucaoRepository = resolucaoRepository;
        this.resolucaoMapper = resolucaoMapper;
    }

    @Override
    public Resolucao salvar(Resolucao resolucao) {
        ResolucaoJpaEntity entity = resolucaoMapper.toEntity(resolucao);
        ResolucaoJpaEntity savedEntity = resolucaoRepository.save(entity);
        return resolucaoMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Resolucao> buscarPorId(UUID id) {
        return resolucaoRepository.findById(id).map(resolucaoMapper::toDomain);
    }

    @Override
    public List<Resolucao> listarPorSistema(UUID sistemaId) {
        return resolucaoRepository.findBySistemaId(sistemaId).stream()
                .map(resolucaoMapper::toDomain)
                .toList();
    }

    @Override
    public void deletar(UUID id) {
        resolucaoRepository.deleteById(id);
    }

    /** Nome antigo, mantido para quem ainda chama. */
    public List<Resolucao> buscarPorSistemaId(UUID sistemaId) {
        return listarPorSistema(sistemaId);
    }
}