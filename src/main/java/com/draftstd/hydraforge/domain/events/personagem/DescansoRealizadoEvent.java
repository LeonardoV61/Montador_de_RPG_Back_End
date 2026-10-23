package com.draftstd.hydraforge.domain.events.personagem;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record DescansoRealizadoEvent(UUID eventId, String aggregateId, String tipoDescanso, long version, Instant occurredOn, UUID sessaoId) implements DomainEvent {
    @Override public UUID getEventId() { return eventId; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    @Override public long getVersion() { return version; }
}