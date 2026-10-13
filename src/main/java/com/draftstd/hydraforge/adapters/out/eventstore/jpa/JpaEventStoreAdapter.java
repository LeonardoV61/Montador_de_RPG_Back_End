package com.draftstd.hydraforge.adapters.out.eventstore.jpa;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.ports.out.eventstore.EventStorePort;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JpaEventStoreAdapter implements EventStorePort {

    private final SpringDataEventStoreRepository repository;
    private final ObjectMapper objectMapper;

    public JpaEventStoreAdapter(SpringDataEventStoreRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveEvents(String aggregateId, List<DomainEvent> events, long expectedVersion) {
        // Lógica de persistência na event_store com mapeamento JSON do payload
        for (DomainEvent event : events) {
            EventStoreEntity entity = new EventStoreEntity();
            entity.setAggregateId(aggregateId);
            entity.setEventType(event.getClass().getName());
            entity.setVersion(event.getVersion());
            entity.setOccurredOn(event.getOccurredOn());
            try {
                entity.setPayload(objectMapper.writeValueAsString(event));
            } catch (Exception e) {
                throw new RuntimeException("Erro ao serializar evento", e);
            }
            repository.save(entity);
        }
    }

    @Override
    public List<DomainEvent> getEventsForAggregate(String aggregateId) {
        // Implementação de recuperação e desserialização reversa
        return List.of();
    }
}