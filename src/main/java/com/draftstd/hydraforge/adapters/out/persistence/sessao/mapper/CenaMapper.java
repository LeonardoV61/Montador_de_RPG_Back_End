package com.draftstd.hydraforge.adapters.out.persistence.sessao.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.CenaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.SessaoJpaEntity;
import com.draftstd.hydraforge.domain.model.sessao.Cena;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CenaMapper {

    public Cena toDomain(CenaJpaEntity entity) {
        if (entity == null) return null;

        return new Cena(
                entity.getId(),
                entity.getSessao() != null ? entity.getSessao().getId() : null,
                entity.getTitulo(),
                entity.getMapaJson(),
                entity.getUrlMapa(),
                entity.getOrdem(),
                entity.getTipo(),
                entity.getEstado(),
                entity.getAtiva(),
                entity.getCriadaEm(),
                List.of()
        );
    }

    public CenaJpaEntity toEntity(Cena domain) {
        if (domain == null) return null;

        CenaJpaEntity entity = new CenaJpaEntity();
        entity.setId(domain.getId());
        entity.setTitulo(domain.getTitulo());
        entity.setMapaJson(domain.getMapaJson());
        entity.setUrlMapa(domain.getUrlMapa());
        entity.setOrdem(domain.getOrdem());
        entity.setTipo(domain.getTipo());
        entity.setEstado(domain.getEstadoJson());
        entity.setAtiva(domain.getAtiva());
        entity.setCriadaEm(domain.getCriadaEm());

        if (domain.getSessaoId() != null) {
            SessaoJpaEntity sessaoEntity = new SessaoJpaEntity();
            sessaoEntity.setId(domain.getSessaoId());
            entity.setSessao(sessaoEntity);
        }

        return entity;
    }
}