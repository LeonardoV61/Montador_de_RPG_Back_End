package com.draftstd.hydraforge.domain.events.campanha;

import com.draftstd.hydraforge.domain.events.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record MembroCampanhaRemovidoEvent(
    UUID eventId,
    String aggregateId,
    Instant occurredOn,
    long version,
    UUID sessaoId,
    UUID usuarioId
) implements DomainEvent {

    public MembroCampanhaRemovidoEvent(UUID campanhaId, UUID usuarioId, long version, UUID sessaoId) {
        this(
            UUID.randomUUID(),
            campanhaId.toString(),
            Instant.now(),
            version,
            sessaoId != null ? sessaoId : campanhaId,
            usuarioId
        );
    }

    @Override
    public UUID getEventId() { return eventId; }

    @Override
    public String getAggregateId() { return aggregateId; }

    @Override
    public Instant getOccurredOn() { return occurredOn; }

    @Override
    public long getVersion() { return version; }
}