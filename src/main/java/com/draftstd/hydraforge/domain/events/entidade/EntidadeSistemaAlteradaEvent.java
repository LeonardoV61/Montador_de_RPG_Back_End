package com.draftstd.hydraforge.domain.events.entidade;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import java.time.Instant;
import java.util.UUID;

record EntidadeSistemaAlteradaEvent(UUID eventId, String aggregateId, UUID sistemaId, String nome, String tipo, String acao, long version, Instant occurredOn) implements DomainEvent {
    @Override public UUID getEventId() { return eventId; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    @Override public long getVersion() { return version; }
}
