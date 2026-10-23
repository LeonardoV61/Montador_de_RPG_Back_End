package com.draftstd.hydraforge.domain.events.usuario;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record SolicitacaoAmizadeEnviadaEvent(UUID eventId, String aggregateId, UUID destinatarioId, long version, Instant occurredOn, UUID sessaoId) implements DomainEvent {
    @Override public UUID getEventId() { return eventId; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    @Override public long getVersion() { return version; }
}