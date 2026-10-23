package com.draftstd.hydraforge.adapters.out.persistence.sessao.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.campanha.entity.CampanhaJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.AnotacaoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.SessaoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.usuario.entity.UsuarioJpaEntity;
import com.draftstd.hydraforge.domain.model.sessao.Anotacao;
import org.springframework.stereotype.Component;

@Component
public class AnotacaoMapper {

    public Anotacao toDomain(AnotacaoJpaEntity entity) {
        if (entity == null) return null;

        return new Anotacao(
                entity.getIdAnotacao(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getCampanha() != null ? entity.getCampanha().getId() : null,
                entity.getSessao() != null ? entity.getSessao().getId() : null,
                entity.getTitulo(),
                entity.getConteudo(),
                entity.getCategoria(),
                entity.getAnotacaoComplexa(),
                entity.getEPrivado()
        );
    }

    public AnotacaoJpaEntity toEntity(Anotacao domain) {
        if (domain == null) return null;

        AnotacaoJpaEntity entity = new AnotacaoJpaEntity();
        entity.setIdAnotacao(domain.getId());
        entity.setTitulo(domain.getTitulo());
        entity.setConteudo(domain.getConteudo());
        entity.setCategoria(domain.getCategoria());
        entity.setAnotacaoComplexa(domain.getAnotacaoComplexaJson());
        entity.setEPrivado(domain.getEPrivado());
        entity.setCriadaEm(domain.getCriadaEm());

        if (domain.getUsuarioId() != null) {
            UsuarioJpaEntity usuarioEntity = new UsuarioJpaEntity();
            usuarioEntity.setId(domain.getUsuarioId());
            entity.setUsuario(usuarioEntity);
        }

        if (domain.getCampanhaId() != null) {
            CampanhaJpaEntity campanhaEntity = new CampanhaJpaEntity();
            campanhaEntity.setId(domain.getCampanhaId());
            entity.setCampanha(campanhaEntity);
        }

        if (domain.getSessaoId() != null) {
            SessaoJpaEntity sessaoEntity = new SessaoJpaEntity();
            sessaoEntity.setId(domain.getSessaoId());
            entity.setSessao(sessaoEntity);
        }

        return entity;
    }
}