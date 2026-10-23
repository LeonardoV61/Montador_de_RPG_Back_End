package com.draftstd.hydraforge.adapters.out.persistence.campanha.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaMembroJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaMembroKeyJpaEntity;
import com.draftstd.hydraforge.domain.model.campanha.CampanhaMembro;
import org.springframework.stereotype.Component;

@Component
public class CampanhaMembroMapper {

    public CampanhaMembro toDomain(CampanhaMembroJpaEntity entity) {
        if (entity == null) return null;

        return new CampanhaMembro(
                entity.getId().getIdUsuario(),
                entity.getPapel(),
                entity.getStatus(),
                entity.getEntrouEm()
        );
    }

    public CampanhaMembroJpaEntity toEntity(CampanhaJpaEntity campanhaEntity, CampanhaMembro domain) {
        if (domain == null) return null;

        CampanhaMembroJpaEntity entity = new CampanhaMembroJpaEntity();
        entity.setId(new CampanhaMembroKeyJpaEntity(campanhaEntity.getId(), domain.usuarioId()));
        entity.setCampanha(campanhaEntity);
        entity.setPapel(domain.papel());
        entity.setStatus(domain.status());
        entity.setEntrouEm(domain.entrouEm());

        return entity;
    }
}