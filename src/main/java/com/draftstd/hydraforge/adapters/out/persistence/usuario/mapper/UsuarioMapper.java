package com.draftstd.hydraforge.adapters.out.persistence.usuario.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.usuario.entity.UsuarioJpaEntity;
import com.draftstd.hydraforge.domain.events.usuario.UsuarioRegistradoEvent;
import com.draftstd.hydraforge.domain.model.usuario.Usuario;

import java.time.ZoneOffset;
import java.util.List;

public class UsuarioMapper {

    public static UsuarioJpaEntity toEntity(Usuario domain) {
        if (domain == null) return null;

        UsuarioJpaEntity entity = new UsuarioJpaEntity();
        entity.setId(domain.getId());
        entity.setSenha(domain.getSenha());
        entity.setEmail(domain.getEmail());
        entity.setApelido(domain.getApelido());
        entity.setUrlImagem(domain.getUrlImagem());
        entity.setEAdmin(domain.isEAdmin());
        entity.setProvider(domain.getProvider());
        entity.setProviderId(domain.getProviderId());
        entity.setCriadoEm(domain.getCriadoEm());
        entity.setAtualizadoEm(domain.getAtualizadoEm());
        return entity;
    }

    public static Usuario toDomain(UsuarioJpaEntity entity) {
        if (entity == null) return null;

        Usuario usuario = new Usuario();
        usuario.loadFromHistory(List.of(
            new UsuarioRegistradoEvent(
                java.util.UUID.randomUUID(),
                entity.getId().toString(),
                entity.getEmail(),
                entity.getApelido(),
                entity.getProvider(),
                entity.getProviderId(),
                entity.isEAdmin(),
                1L,
                entity.getCriadoEm() != null ? entity.getCriadoEm().toInstant(ZoneOffset.UTC) : java.time.Instant.now(),
                null
            )
        ));
        usuario.markEventsAsCommitted();
        return usuario;
    }
}