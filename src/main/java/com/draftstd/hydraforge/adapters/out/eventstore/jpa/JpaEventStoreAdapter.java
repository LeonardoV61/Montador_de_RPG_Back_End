package com.draftstd.hydraforge.adapters.out.eventstore.jpa;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
public class JpaEventStoreAdapter implements EventStorePort {

    private final EventStoreRepository repository;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper; // Serializador nativo do Spring

    public JpaEventStoreAdapter(EventStoreRepository repository, ApplicationEventPublisher eventPublisher, ObjectMapper objectMapper) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void saveEvents(String aggregateId, List<DomainEvent> events, long expectedVersion) {
        // 1. Salva os eventos na tabela imutável da EventStore
        for (DomainEvent event : events) {
            try {
                EventStoreEntity entity = new EventStoreEntity();
                entity.setAggregateId(aggregateId);
                entity.setEventType(event.getClass().getName());
                entity.setPayload(objectMapper.writeValueAsString(event));
                entity.setVersion(expectedVersion);
                entity.setOccurredOn(Instant.now());

                repository.save(entity);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao serializar/persistir evento na EventStore", e);
            }
        }

        // 2. Despacha no barramento interno de aplicação (Spring ApplicationEventPublisher)
        for (DomainEvent event : events) {
            eventPublisher.publishEvent(event);
        }
    }

    @Override
    public List<DomainEvent> getEventsForAggregate(String aggregateId) {
        // Busca o histórico de eventos para reconstrução/reidratação do Agregado
        return repository.findByAggregateId(aggregateId)
                .stream()
                .map(entity -> {
                    try {
                        return objectMapper.readValue(entity.getPayload(), Class.forName(entity.getEventType()));
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao desserializar evento da EventStore", e);
                    }
                })
                .toList();
        return List.of();
    }
}