package com.draftstd.hydraforge.adapters.out.persistence.sessao.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.CenaParticipantesJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.CenaParticipantesKeyJpaEntity;
import com.draftstd.hydraforge.domain.model.sessao.CenaParticipante;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CenaParticipanteMapper {

    public CenaParticipante toDomain(CenaParticipantesJpaEntity entity) {
        if (entity == null) return null;

        return new CenaParticipante(
                entity.getId().getInstanciaId(),
                entity.getPosicaoJson(),
                entity.getLado()
        );
    }

    public CenaParticipantesJpaEntity toEntity(CenaParticipante domain, UUID cenaId) {
        if (domain == null) return null;

        CenaParticipantesJpaEntity entity = new CenaParticipantesJpaEntity();
        entity.setId(new CenaParticipantesKeyJpaEntity(cenaId, domain.instanciaId()));
        entity.setPosicaoJson(domain.posicaoJson());
        entity.setLado(domain.lado());

        return entity;
    }
}