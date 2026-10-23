package com.draftstd.hydraforge.adapters.out.persistence.batalha.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.batalha.entity.BatalhaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.batalha.mapper.BatalhaMapper;
import com.draftstd.hydraforge.adapters.out.persistence.batalha.repository.BatalhaRepository;
import com.draftstd.hydraforge.domain.enums.BatalhaStatus;
import com.draftstd.hydraforge.domain.model.batalha.Batalha;
import com.draftstd.hydraforge.ports.out.persistence.batalha.BatalhaOutputPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class BatalhaPersistenceAdapter implements BatalhaOutputPort {

    private final BatalhaRepository batalhaRepository;
    private final BatalhaMapper batalhaMapper; 

    public BatalhaPersistenceAdapter(BatalhaRepository batalhaRepository, BatalhaMapper batalhaMapper) {
        this.batalhaRepository = batalhaRepository;
        this.batalhaMapper = batalhaMapper;
    }

    @Override
    public Batalha salvar(Batalha batalha) {
        BatalhaJpaEntity entity = batalhaMapper.toEntity(batalha);
        BatalhaJpaEntity salva = batalhaRepository.save(entity);
        return batalhaMapper.toDomain(salva);
    }

    @Override
    public Optional<Batalha> buscarPorId(UUID id) {
        return batalhaRepository.findById(id)
                .map(batalhaMapper::toDomain);
    }

    @Override
    public Optional<Batalha> buscarAtivaPorCenaId(UUID cenaId) {
        return batalhaRepository.findByCenaIdAndStatus(cenaId, BatalhaStatus.EM_ANDAMENTO)
                .map(batalhaMapper::toDomain);
    }

    @Override
    public void deletarPorId(UUID id) {
        batalhaRepository.deleteById(id);
    }
}