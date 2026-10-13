package com.draftstd.hydraforge.domain.events.sessao;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.domain.enums.StatusSessao; // ou o tipo Enum de status que você utiliza
import java.time.Instant;
import java.util.UUID;

public record StatusSessaoAlteradoEvent(
        UUID eventId,
        String aggregateId,
        UUID sessaoId,
        StatusSessao novoStatus,
        long version,
        Instant occurredOn
) implements DomainEvent {
    @Override public UUID getEventId() { return eventId; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    @Override public long getVersion() { return version; }
}