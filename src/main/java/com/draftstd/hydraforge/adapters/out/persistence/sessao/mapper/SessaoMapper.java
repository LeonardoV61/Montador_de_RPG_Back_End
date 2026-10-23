package com.draftstd.hydraforge.adapters.out.persistence.sessao.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.SessaoJpaEntity;
import com.draftstd.hydraforge.domain.events.sessao.SessaoCriadaEvent;
import com.draftstd.hydraforge.domain.model.sessao.Sessao;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class SessaoMapper {

    public Sessao toDomain(SessaoJpaEntity entity) {
        if (entity == null) return null;

        Sessao sessao = new Sessao();
        
        // Reidratação inicial com evento base
        SessaoCriadaEvent eventoCriacao = new SessaoCriadaEvent(
                UUID.randomUUID(),
                entity.getId().toString(),
                entity.getId(),
                entity.getCampanha() != null ? entity.getCampanha().getId() : null,
                entity.getTitulo(),
                entity.getOrdem(),
                1L,
                Instant.now()
        );

        sessao.loadFromHistory(java.util.List.of(eventoCriacao));
        if (entity.getProcedimentoAtivoId() != null) {
            sessao.definirProcedimentoAtivo(entity.getProcedimentoAtivoId());
        }
        sessao.pullEventos(); // Limpa o evento gerado na reidratação

        return sessao;
    }

    public SessaoJpaEntity toEntity(Sessao domain) {
        if (domain == null) return null;

        SessaoJpaEntity entity = new SessaoJpaEntity();
        entity.setId(domain.getId());
        entity.setTitulo(domain.getTitulo());
        entity.setDataInicio(domain.getDataInicio());
        entity.setDataFim(domain.getDataFim());
        entity.setSessaoSnapshot(domain.getSessaoSnapshotJson());
        entity.setStatus(domain.getStatus());
        entity.setOrdem(domain.getOrdem());
        entity.setProcedimentoAtivoId(domain.getProcedimentoAtivoId());

        if (domain.getCampanhaId() != null) {
            CampanhaJpaEntity campanhaEntity = new CampanhaJpaEntity();
            campanhaEntity.setId(domain.getCampanhaId());
            entity.setCampanha(campanhaEntity);
        }

        return entity;
    }
}