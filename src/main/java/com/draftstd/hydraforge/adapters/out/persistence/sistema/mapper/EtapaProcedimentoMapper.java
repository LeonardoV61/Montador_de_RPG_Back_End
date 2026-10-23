package com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.EtapaProcedimentoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.ProcedimentoJpaEntity;
import com.draftstd.hydraforge.domain.model.sistema.EtapaProcedimento;
import org.springframework.stereotype.Component;

@Component
public class EtapaProcedimentoMapper {

    public EtapaProcedimentoJpaEntity toEntity(EtapaProcedimento domain, ProcedimentoJpaEntity procedimentoJpa) {
        if (domain == null) return null;
        EtapaProcedimentoJpaEntity entity = new EtapaProcedimentoJpaEntity();
        entity.setIdEtapa(domain.getIdEtapa());
        entity.setProcedimento(procedimentoJpa);
        entity.setOrdem(domain.getOrdem());
        entity.setNome(domain.getNome());
        entity.setTipoEtapa(domain.getTipoEtapa());
        entity.setParametrosEtapa(JsonMapperUtil.toJsonNode(domain.getParametrosEtapa()));
        entity.setObrigatorio(domain.isObrigatorio());
        return entity;
    }

    public EtapaProcedimento toDomain(EtapaProcedimentoJpaEntity entity) {
        if (entity == null) return null;
        return new EtapaProcedimento(
            entity.getIdEtapa(),
            entity.getProcedimento() != null ? entity.getProcedimento().getId() : null,
            entity.getOrdem(),
            entity.getNome(),
            entity.getTipoEtapa(),
            JsonMapperUtil.toMap(entity.getParametrosEtapa()),
            entity.getObrigatorio()
        );
    }
}