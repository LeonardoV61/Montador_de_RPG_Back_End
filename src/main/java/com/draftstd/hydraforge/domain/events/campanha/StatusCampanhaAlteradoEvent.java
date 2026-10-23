package com.draftstd.hydraforge.domain.events.campanha;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.domain.enums.StatusCampanha;

import java.time.Instant;
import java.util.UUID;

public record StatusCampanhaAlteradoEvent(
    UUID eventId,
    String aggregateId,
    Instant occurredOn,
    long version,
    UUID sessaoId,
    StatusCampanha statusAnterior,
    StatusCampanha statusNovo
) implements DomainEvent {

    public StatusCampanhaAlteradoEvent(UUID campanhaId, StatusCampanha statusAnterior, StatusCampanha statusNovo, long version, UUID sessaoId) {
        this(
            UUID.randomUUID(),
            campanhaId.toString(),
            Instant.now(),
            version,
            sessaoId != null ? sessaoId : campanhaId,
            statusAnterior,
            statusNovo
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