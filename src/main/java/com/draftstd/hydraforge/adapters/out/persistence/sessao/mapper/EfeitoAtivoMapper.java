package com.draftstd.hydraforge.adapters.out.persistence.sessao.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.EfeitoAtivoJpaEntity;
import com.draftstd.hydraforge.adapters.out.persistence.sessao.entity.SessaoJpaEntity;
import com.draftstd.hydraforge.domain.model.sessao.EfeitoAtivo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EfeitoAtivoMapper {

    public EfeitoAtivo toDomain(EfeitoAtivoJpaEntity entity) {
        if (entity == null) return null;

        return new EfeitoAtivo(
                entity.getId(),
                entity.getEntidadeInstanciaId(),
                entity.getEntidadeProcedimentoId(),
                entity.getParametrosJson(),
                entity.getMomento(),
                entity.getExpiraEm(),
                entity.getAtivo(),
                entity.getUsosRestantes()
        );
    }

    public EfeitoAtivoJpaEntity toEntity(EfeitoAtivo domain, UUID sessaoId) {
        if (domain == null) return null;

        EfeitoAtivoJpaEntity entity = new EfeitoAtivoJpaEntity();
        entity.setId(domain.getId());

        if (sessaoId != null) {
            SessaoJpaEntity sessaoEntity = new SessaoJpaEntity();
            sessaoEntity.setId(sessaoId);
            entity.setSessao(sessaoEntity);
        }

        entity.setEntidadeInstanciaId(domain.getEntidadeInstanciaId());
        entity.setEntidadeProcedimentoId(domain.getEntidadeProcedimentoId());
        entity.setParametrosJson(domain.getParametrosJson());
        entity.setMomento(domain.getMomento());
        entity.setExpiraEm(domain.getExpiraEm());
        entity.setAtivo(domain.isAtivo());
        entity.setUsosRestantes(domain.getUsosRestantes());

        return entity;
    }
}