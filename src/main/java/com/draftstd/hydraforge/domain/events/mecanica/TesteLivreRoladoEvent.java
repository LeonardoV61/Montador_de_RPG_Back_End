package com.draftstd.hydraforge.domain.events.mecanica;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record TesteLivreRoladoEvent(UUID eventId, String aggregateId, UUID personagemId, String formula, int resultadoTotal, long version, Instant occurredOn) implements DomainEvent {
    @Override public UUID getEventId() { return eventId; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    @Override public long getVersion() { return version; }
}