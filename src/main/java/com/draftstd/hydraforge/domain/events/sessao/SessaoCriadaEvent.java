package com.draftstd.hydraforge.domain.events.sessao;

import com.draftstd.hydraforge.domain.events.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record SessaoCriadaEvent(
        UUID eventId,
        String aggregateId,
        UUID sessaoId,
        UUID campanhaId,
        String titulo,
        int ordem,
        long version,
        Instant occurredOn
) implements DomainEvent {

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public Instant getOccurredOn() {
        return occurredOn;
    }

    @Override
    public long getVersion() {
        return version;
    }
}