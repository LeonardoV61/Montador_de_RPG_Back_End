package com.draftstd.hydraforge.adapters.out.eventstore.adapter;

import com.draftstd.hydraforge.adapters.out.eventstore.entity.EventStoreJpaEntity;
import com.draftstd.hydraforge.adapters.out.eventstore.repository.EventStoreRepository;
import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.domain.events.HasSessao;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class JpaEventStoreAdapter implements EventStorePort {

    private final EventStoreRepository eventoSistemaRepository;
    private final ObjectMapper objectMapper;

    public JpaEventStoreAdapter(EventStoreRepository eventoSistemaRepository, ObjectMapper objectMapper) {
        this.eventoSistemaRepository = eventoSistemaRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void salvarEventos(String aggregateId, List<DomainEvent> eventos, long versaoEsperada) {
        if (eventos == null || eventos.isEmpty()) {
            return;
        }

        List<EventStoreJpaEntity> entidades = new ArrayList<>();

        for (DomainEvent event : eventos) {
            EventStoreJpaEntity entity = new EventStoreJpaEntity();
            entity.setIdEvento(event.getEventId());
            entity.setAggregateId(aggregateId);
            
            // Extração robusta do sessaoId com fallback para o aggregateId se for um UUID válido
            UUID sessaoId = null;
            if (event instanceof HasSessao eventoComSessao) {
                sessaoId = eventoComSessao.getSessaoId();
            }
            if (sessaoId == null && aggregateId != null) {
                try {
                    sessaoId = UUID.fromString(aggregateId);
                } catch (IllegalArgumentException ignored) {
                    // O aggregateId não é um UUID válido, mantém null
                }
            }
            entity.setSessaoId(sessaoId);
            
            entity.setTipoEvento(event.getClass().getName());
            entity.setVersao(event.getVersion());
            entity.setDataHora(event.getOccurredOn());

            try {
                entity.setPayloadJson(objectMapper.writeValueAsString(event));
            } catch (JacksonException e) {
                throw new IllegalStateException("Falha ao serializar o evento de domínio: " + event.getClass().getSimpleName(), e);
            }

            entidades.add(entity);
        }

        eventoSistemaRepository.saveAll(entidades);
    }

    @Override
    public List<DomainEvent> carregarEventos(String aggregateId) {
        List<EventStoreJpaEntity> entities = eventoSistemaRepository.findByAggregateIdOrderByVersaoAsc(aggregateId);
        return deserializarEventos(entities);
    }

    @Override
    public List<DomainEvent> carregarEventosPorSessao(UUID sessaoId) {
        List<EventStoreJpaEntity> entities = eventoSistemaRepository.findBySessaoIdOrderByVersaoAsc(sessaoId);
        return deserializarEventos(entities);
    }

    private List<DomainEvent> deserializarEventos(List<EventStoreJpaEntity> entities) {
        List<DomainEvent> eventos = new ArrayList<>();

        for (EventStoreJpaEntity entity : entities) {
            try {
                Class<?> eventClass = Class.forName(entity.getTipoEvento());
                DomainEvent event = (DomainEvent) objectMapper.readValue(entity.getPayloadJson(), eventClass);
                eventos.add(event);
            } catch (ClassNotFoundException | JacksonException e) {
                throw new IllegalStateException("Falha ao deserializar evento do tipo: " + entity.getTipoEvento(), e);
            }
        }
        return eventos;
    }
}