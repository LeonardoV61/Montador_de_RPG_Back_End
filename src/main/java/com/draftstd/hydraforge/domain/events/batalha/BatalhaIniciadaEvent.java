// Pacote: com.draftstd.hydraforge.domain.events.batalha
package com.draftstd.hydraforge.domain.events.batalha;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record BatalhaIniciadaEvent(UUID eventId, String aggregateId, UUID sessaoId, UUID cenaId, long version, Instant occurredOn) implements DomainEvent {
    @Override public UUID getEventId() { return eventId; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    @Override public long getVersion() { return version; }
}