package com.draftstd.hydraforge.adapters.out.persistence.campanha.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaMembroJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaMembroKeyJpaEntity;
import com.draftstd.hydraforge.domain.model.campanha.Campanha;
import com.draftstd.hydraforge.domain.model.campanha.CampanhaMembro;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CampanhaMapper {

    public Campanha toDomain(CampanhaJpaEntity entity) {
        if (entity == null) return null;

        List<CampanhaMembro> membros = (entity.getMembros() != null) ?
                entity.getMembros().stream().map(this::toDomainParticipante).toList() : List.of();

        return new Campanha(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getUrlImagem(),
                entity.getStatus(),
                entity.getSistemaId(),
                entity.getMestreId(),
                entity.getCriadaEm(),
                entity.getAtualizadaEm(),
                membros,
                List.of(), // sessoesIds
                List.of(), // personagensIds
                List.of(), // instanciasIds
                entity.getVersion()
        );
    }

    public CampanhaJpaEntity toEntity(Campanha domain) {
        if (domain == null) return null;

        CampanhaJpaEntity entity = new CampanhaJpaEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setDescricao(domain.getDescricao());
        entity.setUrlImagem(domain.getUrlImagem());
        entity.setStatus(domain.getStatus());
        entity.setSistemaId(domain.getSistemaId());
        entity.setMestreId(domain.getMestreId());
        entity.setCriadaEm(domain.getCriadaEm());
        entity.setAtualizadaEm(domain.getAtualizadaEm());
        entity.setVersion(domain.getVersion());

        if (domain.getMembros() != null) {
            List<CampanhaMembroJpaEntity> membrosEntities = domain.getMembros().stream()
                    .map(m -> toEntityParticipante(entity, m))
                    .toList();
            entity.setMembros(membrosEntities);
        }

        return entity;
    }

    public CampanhaMembro toDomainParticipante(CampanhaMembroJpaEntity entity) {
        if (entity == null) return null;

        return new CampanhaMembro(
                entity.getId().getIdUsuario(),
                entity.getPapel(),
                entity.getStatus(),
                entity.getEntrouEm()
        );
    }

    public CampanhaMembroJpaEntity toEntityParticipante(CampanhaJpaEntity campanhaEntity, CampanhaMembro domainMembro) {
        if (domainMembro == null) return null;

        CampanhaMembroJpaEntity entity = new CampanhaMembroJpaEntity();
        entity.setId(new CampanhaMembroKeyJpaEntity(campanhaEntity.getId(), domainMembro.usuarioId()));
        entity.setCampanha(campanhaEntity);
        entity.setPapel(domainMembro.papel());
        entity.setStatus(domainMembro.status());
        entity.setEntrouEm(domainMembro.entrouEm());

        return entity;
    }
}