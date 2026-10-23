package com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.*;
import com.draftstd.hydraforge.domain.model.sistema.entidade.EntidadeProcedimento;

import org.springframework.stereotype.Component;

@Component
public class EntidadeProcedimentoMapper {

    public EntidadeProcedimentoJpaEntity toEntity(EntidadeProcedimento domain,
                                                  SistemaJpaEntity sistemaJpa,
                                                  EntidadeSistemaJpaEntity entidadeSistemaJpa,
                                                  EntidadeInstanciaJpaEntity instanciaJpa,
                                                  ProcedimentoJpaEntity procedimentoJpa) {
        if (domain == null) return null;
        EntidadeProcedimentoJpaEntity entity = new EntidadeProcedimentoJpaEntity();
        entity.setId(domain.getId());
        entity.setSistema(sistemaJpa);
        entity.setEntidadeSistema(entidadeSistemaJpa);
        entity.setEntidadeInstancia(instanciaJpa);
        entity.setProcedimento(procedimentoJpa);
        entity.setProcessamento(domain.getProcessamento());
        entity.setCondicao(JsonMapperUtil.toJsonNode(domain.getCondicao()));
        entity.setEReativo(domain.isEReativo());
        entity.setOrdem(domain.getOrdem());
        return entity;
    }

    public EntidadeProcedimento toDomain(EntidadeProcedimentoJpaEntity entity) {
        if (entity == null) return null;
        return new EntidadeProcedimento(
            entity.getId(),
            entity.getSistema() != null ? entity.getSistema().getId() : null,
            entity.getEntidadeSistema() != null ? entity.getEntidadeSistema().getId() : null,
            entity.getEntidadeInstancia() != null ? entity.getEntidadeInstancia().getId() : null,
            entity.getProcedimento() != null ? entity.getProcedimento().getId() : null,
            entity.getProcessamento(),
            JsonMapperUtil.toMap(entity.getCondicao()),
            entity.isEReativo(),
            entity.getOrdem()
        );
    }
}