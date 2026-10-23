package com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeInstanciaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeSistemaJpaEntity;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeInstancia;

import org.springframework.stereotype.Component;

@Component
public class EntidadeInstanciaMapper {

    public EntidadeInstanciaJpaEntity toEntity(EntidadeInstancia domain, EntidadeSistemaJpaEntity entidadeSistemaJpa) {
        if (domain == null) return null;
        EntidadeInstanciaJpaEntity entity = new EntidadeInstanciaJpaEntity();
        entity.setId(domain.getId());
        entity.setEntidadeSistema(entidadeSistemaJpa);
        entity.setCampanhaId(domain.getCampanhaId());
        entity.setDonoId(domain.getDonoId());
        entity.setTipo(domain.getTipo());
        entity.setNome(domain.getNome());
        entity.setDescricao(domain.getDescricao());
        entity.setUrlImagem(domain.getUrlImagem());
        entity.setAtributosAtuais(JsonMapperUtil.toJsonNode(domain.getValoresAtributos()));
        entity.setCustomizacoes(JsonMapperUtil.toJsonNode(domain.getCustomizacoes()));
        entity.setCriadaEm(domain.getCriadaEm());
        return entity;
    }

    public EntidadeInstancia toDomain(EntidadeInstanciaJpaEntity entity) {
        if (entity == null) return null;
        return new EntidadeInstancia(
            entity.getId(),
            entity.getCampanhaId(),
            entity.getEntidadeSistema() != null ? entity.getEntidadeSistema().getId() : null,
            entity.getDonoId(),
            entity.getTipo(),
            entity.getNome(),
            entity.getDescricao(),
            entity.getUrlImagem(),
            JsonMapperUtil.toMap(entity.getAtributosAtuais()),
            JsonMapperUtil.toMap(entity.getCustomizacoes()),
            entity.getCriadaEm()
        );
    }
}