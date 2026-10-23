package com.draftstd.hydraforge.adapters.out.persistence.sessao.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.HistoricoAcoesJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.SessaoJpaEntity;
import com.draftstd.hydraforge.domain.model.sessao.HistoricoAcoes;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HistoricoAcoesMapper {

    public HistoricoAcoes toDomain(HistoricoAcoesJpaEntity entity) {
        if (entity == null) return null;

        return new HistoricoAcoes(
                entity.getId(),
                entity.getEntidadeInstanciaId(),
                entity.getMomento(),
                entity.getResultadoJson()
        );
    }

    public HistoricoAcoesJpaEntity toEntity(HistoricoAcoes domain, UUID sessaoId) {
        if (domain == null) return null;

        HistoricoAcoesJpaEntity entity = new HistoricoAcoesJpaEntity();
        entity.setId(domain.getId());
        entity.setEntidadeInstanciaId(domain.entidadeInstanciaId());
        entity.setMomento(domain.momento());
        entity.setResultadoJson(domain.resultadoJson());

        if (sessaoId != null) {
            SessaoJpaEntity sessaoEntity = new SessaoJpaEntity();
            sessaoEntity.setId(sessaoId);
            entity.setSessao(sessaoEntity);
        }

        return entity;
    }
}