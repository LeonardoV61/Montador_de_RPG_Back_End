package com.draftstd.hydraforge.adapters.out.persistence.usuario.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.usuario.entity.AmizadeJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.usuario.entity.AmizadeKeyEmbeddable;
import com.draftstd.hydraforge.domain.model.usuario.Amizade;

public class AmizadeMapper {

    public static AmizadeJpaEntity toEntity(Amizade domain) {
        if (domain == null) return null;

        AmizadeKeyEmbeddable key = new AmizadeKeyEmbeddable(domain.getIdRemetente(), domain.getIdDestinatario());
        return new AmizadeJpaEntity(
            key,
            domain.getStatus(),
            domain.getCriadaEm(),
            domain.getAceitaEm()
        );
    }

    public static Amizade toDomain(AmizadeJpaEntity entity) {
        if (entity == null) return null;

        return new Amizade(
            entity.getId().getIdRemetente(),
            entity.getId().getIdDestinatario(),
            entity.getStatus(),
            entity.getCriadaEm(),
            entity.getAceitoEm()
        );
    }
}