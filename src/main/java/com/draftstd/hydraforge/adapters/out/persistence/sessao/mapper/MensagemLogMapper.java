package com.draftstd.hydraforge.adapters.out.persistence.sessao.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.MensagemLogJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.SessaoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.usuario.entity.UsuarioJpaEntity;
import com.draftstd.hydraforge.domain.model.sessao.MensagemLog;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MensagemLogMapper {

    public MensagemLog toDomain(MensagemLogJpaEntity entity) {
        if (entity == null) return null;

        return new MensagemLog(
                entity.getId(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getConteudo(),
                entity.getMomento()
        );
    }

    public MensagemLogJpaEntity toEntity(MensagemLog domain, UUID sessaoId) {
        if (domain == null) return null;

        MensagemLogJpaEntity entity = new MensagemLogJpaEntity();
        entity.setId(domain.id());
        entity.setConteudo(domain.conteudo());
        entity.setMomento(domain.momento());

        if (sessaoId != null) {
            SessaoJpaEntity sessaoEntity = new SessaoJpaEntity();
            sessaoEntity.setId(sessaoId);
            entity.setSessao(sessaoEntity);
        }

        if (domain.usuarioId() != null) {
            UsuarioJpaEntity usuarioEntity = new UsuarioJpaEntity();
            usuarioEntity.setId(domain.usuarioId());
            entity.setUsuario(usuarioEntity);
        }

        return entity;
    }
}