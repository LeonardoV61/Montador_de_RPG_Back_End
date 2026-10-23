package com.draftstd.hydraforge.adapters.out.persistence.batalha.mapper; 

import com.draftstd.hydraforge.adapters.out.persistence.batalha.entity.BatalhaJpaEntity;
import com.draftstd.hydraforge.domain.model.batalha.Batalha;
import org.springframework.stereotype.Component;

@Component
public class BatalhaMapper {

    public Batalha toDomain(BatalhaJpaEntity entity) {
        if (entity == null) return null;

        Batalha batalha = new Batalha();
        batalha.setId(entity.getId());
        batalha.setStatus(entity.getStatus());
        // Mapeie os demais atributos conforme a sua entidade Batalha e BatalhaJpaEntity possuem

        return batalha;
    }

    public BatalhaJpaEntity toEntity(Batalha domain) {
        if (domain == null) return null;

        BatalhaJpaEntity entity = new BatalhaJpaEntity();
        entity.setId(domain.getId());
        entity.setStatus(domain.getStatus());
        // Mapeie os demais atributos correspondentes

        return entity;
    }
}