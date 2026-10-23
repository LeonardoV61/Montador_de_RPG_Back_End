package com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeInstanciaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeRelacaoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EntidadeSistemaJpaEntity;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeRelacao;

import org.springframework.stereotype.Component;

@Component
public class EntidadeRelacaoMapper {

    public EntidadeRelacaoJpaEntity toEntity(EntidadeRelacao domain, 
                                            EntidadeInstanciaJpaEntity paiJpa, 
                                            EntidadeInstanciaJpaEntity filhaJpa, 
                                            EntidadeSistemaJpaEntity sistemaFilhaJpa) {
        if (domain == null) return null;
        EntidadeRelacaoJpaEntity entity = new EntidadeRelacaoJpaEntity();
        entity.setId(domain.getId());
        entity.setEntidadePai(paiJpa);
        entity.setEntidadeFilha(filhaJpa);
        entity.setEntidadeSistemaFilha(sistemaFilhaJpa);
        entity.setQuantidade(domain.getQuantidade());
        entity.setCustomizacoes(JsonMapperUtil.toJsonNode(domain.getCustomizacoes()));
        entity.setOrigem(domain.getOrigem());
        return entity;
    }

    public EntidadeRelacao toDomain(EntidadeRelacaoJpaEntity entity) {
        if (entity == null) return null;
        return new EntidadeRelacao(
            entity.getId(),
            entity.getEntidadePai() != null ? entity.getEntidadePai().getId() : null,
            entity.getEntidadeFilha() != null ? entity.getEntidadeFilha().getId() : null,
            entity.getEntidadeSistemaFilha() != null ? entity.getEntidadeSistemaFilha().getId() : null,
            entity.getQuantidade(),
            JsonMapperUtil.toMap(entity.getCustomizacoes()),
            entity.getOrigem()
        );
    }
}