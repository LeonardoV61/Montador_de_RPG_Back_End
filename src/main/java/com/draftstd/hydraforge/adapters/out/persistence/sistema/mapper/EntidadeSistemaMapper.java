package com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeSistemaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.SistemaJpaEntity;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeSistema;

import org.springframework.stereotype.Component;

@Component
public class EntidadeSistemaMapper {

    public EntidadeSistemaJpaEntity toEntity(EntidadeSistema domain, SistemaJpaEntity sistemaJpaEntity) {
        if (domain == null) return null;
        EntidadeSistemaJpaEntity entity = new EntidadeSistemaJpaEntity();
        entity.setId(domain.getId());
        entity.setSistema(sistemaJpaEntity);
        entity.setTipo(domain.getTipo());
        entity.setNome(domain.getNome());
        entity.setDescricao(domain.getDescricao());
        entity.setUrlImagem(domain.getUrlImagem());
        entity.setAtributos(JsonMapperUtil.toJsonNode(domain.getAtributos()));
        entity.setPropriedades(JsonMapperUtil.toJsonNode(domain.getPropriedades()));
        return entity;
    }

    public EntidadeSistema toDomain(EntidadeSistemaJpaEntity entity) {
        if (entity == null) return null;
        return new EntidadeSistema(
            entity.getId(),
            entity.getSistema() != null ? entity.getSistema().getId() : null,
            entity.getTipo(),
            entity.getNome(),
            entity.getDescricao(),
            entity.getUrlImagem(),
            JsonMapperUtil.toMap(entity.getAtributos()),
            JsonMapperUtil.toMap(entity.getPropriedades())
        );
    }
}