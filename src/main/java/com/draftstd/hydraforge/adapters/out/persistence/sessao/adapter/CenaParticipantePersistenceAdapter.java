package com.draftstd.hydraforge.adapters.out.persistence.sessao.adapter;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.CenaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.CenaParticipantesJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.mapper.CenaParticipanteMapper;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.repository.CenaParticipantesRepository;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.repository.CenaRepository;
import com.draftstd.hydraforge.domain.model.sessao.CenaParticipante;
import com.draftstd.hydraforge.ports.out.persistence.sessao.CenaParticipanteOutputPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class CenaParticipantePersistenceAdapter implements CenaParticipanteOutputPort {

    private final CenaParticipantesRepository repository;
    private final CenaRepository cenaRepository;
    private final CenaParticipanteMapper mapper;

    public CenaParticipantePersistenceAdapter(CenaParticipantesRepository repository,
                                              CenaRepository cenaRepository,
                                              CenaParticipanteMapper mapper) {
        this.repository = repository;
        this.cenaRepository = cenaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void salvar(UUID cenaId, CenaParticipante participante) {
        repository.save(paraEntity(cenaId, participante));
    }

    @Override
    @Transactional
    public void salvarTodos(UUID cenaId, List<CenaParticipante> participantes) {
        if (participantes == null || participantes.isEmpty()) {
            return;
        }
        List<CenaParticipantesJpaEntity> entities = participantes.stream()
                .map(p -> paraEntity(cenaId, p))
                .toList();
        repository.saveAll(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CenaParticipante> buscarPorCenaId(UUID cenaId) {
        CenaJpaEntity cena = cenaRepository.getReferenceById(cenaId);
        return repository.findByCena(cena).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deletarPorCenaId(UUID cenaId) {
        CenaJpaEntity cena = cenaRepository.getReferenceById(cenaId);
        repository.deleteAll(repository.findByCena(cena));
    }

    private CenaParticipantesJpaEntity paraEntity(UUID cenaId, CenaParticipante participante) {
        CenaParticipantesJpaEntity entity = mapper.toEntity(participante, cenaId);
        // O mapper só preenche a chave composta. Se a entity usa @MapsId na relação "cena",
        // a referência precisa estar setada, senão o Hibernate falha ao salvar.
        entity.setCena(cenaRepository.getReferenceById(cenaId));
        return entity;
    }
}