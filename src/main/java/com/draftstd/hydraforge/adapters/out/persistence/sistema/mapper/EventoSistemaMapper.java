// package com.draftstd.hydraforge.adapters.out.persistence.sistema.mapper;

// import com.draftstd.hydraforge.adapters.out.persistence.sistema.entity.SistemaJpaEntity;
// import com.draftstd.hydraforge.domain.model.sistema.EventoSistema;
// import org.springframework.stereotype.Component;

// @Component
// public class EventoSistemaMapper {

//     public EventoSistemaJpaEntity toEntity(EventoSistema domain, SistemaJpaEntity sistemaJpa) {
//         if (domain == null) return null;
//         EventoSistemaJpaEntity entity = new EventoSistemaJpaEntity();
//         entity.setId(domain.getId());
//         entity.setSistema(sistemaJpa);
//         entity.setTipo(domain.getTipo());
//         entity.setNome(domain.getNome());
//         entity.setDescricao(domain.getDescricao());
//         entity.setPayloadSchema((tools.jackson.databind.JsonNode) domain.getPayloadSchema());
//         return entity;
//     }

//     public EventoSistema toDomain(EventoSistemaJpaEntity entity) {
//         if (entity == null) return null;
//         EventoSistema domain = new EventoSistema();
//         domain.setId(entity.getId());
//         domain.setSistemaId(entity.getSistema() != null ? entity.getSistema().getId() : null);
//         domain.setTipo(entity.getTipo());
//         domain.setNome(entity.getNome());
//         domain.setDescricao(entity.getDescricao());
//         domain.setPayloadSchema(entity.getPayloadSchema());
//         return domain;
//     }
// }