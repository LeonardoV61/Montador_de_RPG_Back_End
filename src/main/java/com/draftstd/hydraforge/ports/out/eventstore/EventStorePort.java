package com.draftstd.hydraforge.ports.out.eventstore;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import java.util.List;

public interface EventStorePort {
    void saveEvents(String aggregateId, List<DomainEvent> events, long expectedVersion);
    List<DomainEvent> getEventsForAggregate(String aggregateId);
}