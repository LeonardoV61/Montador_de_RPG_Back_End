package com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.ProcedimentoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.SistemaJpaEntity;
import com.draftstd.hydraforge.domain.model.sistema.Procedimento;
import org.springframework.stereotype.Component;

@Component
public class ProcedimentoMapper {

    public ProcedimentoJpaEntity toEntity(Procedimento domain, SistemaJpaEntity sistemaJpa) {
        if (domain == null) return null;
        ProcedimentoJpaEntity entity = new ProcedimentoJpaEntity();
        entity.setId(domain.getId());
        entity.setSistema(sistemaJpa);
        entity.setTipo(domain.getTipo());
        entity.setNome(domain.getNome());
        entity.setDescricao(domain.getDescricao());
        entity.setStatus(domain.getStatus());
        entity.setConfigsGeral(JsonMapperUtil.toJsonNode(domain.getConfigsGeral()));
        return entity;
    }

    public Procedimento toDomain(ProcedimentoJpaEntity entity) {
        if (entity == null) return null;
        return new Procedimento(
            entity.getId(),
            entity.getSistema() != null ? entity.getSistema().getId() : null,
            entity.getTipo(),
            entity.getNome(),
            entity.getDescricao(),
            entity.getStatus(),
            JsonMapperUtil.toMap(entity.getConfigsGeral())
        );
    }
}