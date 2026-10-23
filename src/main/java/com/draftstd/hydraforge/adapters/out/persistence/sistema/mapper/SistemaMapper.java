package com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.SistemaJpaEntity;
import com.draftstd.hydraforge.domain.events.sistema.SistemaCriadoEvent;
import com.draftstd.hydraforge.domain.model.sistema.Sistema;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;

@Component
public class SistemaMapper {

    public SistemaJpaEntity toEntity(Sistema domain) {
        if (domain == null) return null;
        SistemaJpaEntity entity = new SistemaJpaEntity();
        entity.setId(domain.getId());
        entity.setUsuarioCriadorId(domain.getUsuarioCriadorId());
        entity.setSistemaPaiId(domain.getSistemaPaiId());
        entity.setNome(domain.getNome());
        entity.setDescricao(domain.getDescricao());
        entity.setUrlImagem(domain.getUrlImagem());
        entity.setConfiguracao(JsonMapperUtil.toJsonNode(domain.getConfiguracao()));
        entity.setSchemaAtributos(JsonMapperUtil.toJsonNode(domain.getSchemaAtributos()));
        entity.setSchemaEntidades(JsonMapperUtil.toJsonNode(domain.getSchemaEntidades()));
        entity.setSchemaResolucoes(JsonMapperUtil.toJsonNode(domain.getSchemaResolucoes()));
        entity.setVersaoSchemas(domain.getVersaoSchemas());
        entity.setEOficial(domain.isEOficial());
        entity.setCriadoEm(domain.getCriadoEm());
        return entity;
    }

    public Sistema toDomain(SistemaJpaEntity entity) {
        if (entity == null) return null;

        Sistema domain = new Sistema();
        domain.loadFromHistory(List.of(
            new SistemaCriadoEvent(
                java.util.UUID.randomUUID(),
                entity.getId().toString(),
                entity.getUsuarioCriadorId(),
                entity.getSistemaPaiId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getUrlImagem(),
                JsonMapperUtil.toMap(entity.getConfiguracao()),
                JsonMapperUtil.toMap(entity.getSchemaAtributos()),
                JsonMapperUtil.toMap(entity.getSchemaEntidades()),
                JsonMapperUtil.toMap(entity.getSchemaResolucoes()),
                entity.isEOficial(),
                1L,
                entity.getCriadoEm() != null ? entity.getCriadoEm().toInstant(ZoneOffset.UTC) : java.time.Instant.now(),
                null
            )
        ));
        domain.markEventsAsCommitted();
        return domain;
    }
}