package com.draftstd.hydraforge.adapters.out.persistence.personagem.mapper;

import com.draftstd.hydraforge.adapters.out.persistence.personagem.entity.PersonagemJpaEntity;
import com.draftstd.hydraforge.domain.model.personagem.Personagem;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PersonagemMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PersonagemJpaEntity toEntity(Personagem domain) {
        if (domain == null) return null;

        PersonagemJpaEntity entity = new PersonagemJpaEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setUsuarioId(domain.getUsuarioId());
        entity.setCampanhaId(domain.getCampanhaId());
        entity.setInstanciaId(domain.getInstanciaId());
        entity.setHistoria(domain.getHistoria());
        entity.setAtivo(domain.isAtivo());
        entity.setAparencia(domain.getAparencia());
        entity.setUrlImagem(domain.getUrlImagem());
        entity.setNotasJogador(domain.getNotasJogador());
        entity.setCriadoEm(domain.getCriadoEm());

        try {
            entity.setAtributosJson(objectMapper.writeValueAsString(domain.getAtributos()));
        } catch (Exception e) {
            entity.setAtributosJson("{}");
        }

        return entity;
    }

    public Personagem toDomain(PersonagemJpaEntity entity) {
        if (entity == null) return null;

        Map<String, Object> atributos = new HashMap<>();
        if (entity.getAtributosJson() != null && !entity.getAtributosJson().isBlank()) {
            try {
                atributos = objectMapper.readValue(entity.getAtributosJson(), Map.class);
            } catch (Exception ignored) {}
        }

        Personagem p = new Personagem();
        // Reidratação usando o método protegido do AggregateRoot
        p.loadFromHistory(java.util.List.of(
            new com.draftstd.hydraforge.domain.events.personagem.PersonagemCriadoEvent(
                java.util.UUID.randomUUID(),
                entity.getId().toString(),
                entity.getUsuarioId(),
                entity.getCampanhaId(),
                entity.getInstanciaId(),
                entity.getNome(),
                entity.getHistoria(),
                entity.getAparencia(),
                entity.getUrlImagem(),
                entity.getNotasJogador(),
                atributos,
                1L,
                java.time.Instant.now(),
                null
            )
        ));
        p.markEventsAsCommitted();
        return p;
    }
}