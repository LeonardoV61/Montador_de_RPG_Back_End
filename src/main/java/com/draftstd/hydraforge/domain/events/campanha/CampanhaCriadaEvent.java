package com.draftstd.hydraforge.domain.events.campanha;

import com.draftstd.hydraforge.domain.events.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CampanhaCriadaEvent(
    UUID eventId,
    String aggregateId,
    Instant occurredOn,
    long version,
    UUID sessaoId,
    UUID sistemaId,
    UUID mestreId,
    String nome
) implements DomainEvent {

    public CampanhaCriadaEvent(UUID campanhaId, UUID sistemaId, UUID mestreId, String nome, long version, UUID sessaoId) {
        this(
            UUID.randomUUID(),
            campanhaId.toString(),
            Instant.now(),
            version,
            sessaoId != null ? sessaoId : campanhaId,
            sistemaId,
            mestreId,
            nome
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