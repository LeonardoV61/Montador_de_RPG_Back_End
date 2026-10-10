package com.draftstd.hydraforge.domain.events.sessao;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.domain.enums.StatusSessao;
import java.time.Instant;
import java.util.UUID;

public record StatusSessaoAlteradoEvent(
    UUID eventId,
    String aggregateId,
    Instant occurredOn,
    long version,
    StatusSessao novoStatus
) implements DomainEvent {
    public StatusSessaoAlteradoEvent(UUID sessaoId, StatusSessao novoStatus, long version) {
        this(UUID.randomUUID(), sessaoId.toString(), Instant.now(), version, novoStatus);
    }

    @Override public UUID getEventId() { return eventId; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    @Override public long getVersion() { return version; }
}