package com.draftstd.hydraforge.domain.events.usuario;

import com.draftstd.hydraforge.domain.enums.StatusAmizade;
import com.draftstd.hydraforge.domain.events.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record SolicitacaoAmizadeRespondidaEvent(UUID eventId, String aggregateId, UUID remetenteId, StatusAmizade novoStatus, long version, Instant occurredOn, UUID sessaoId) implements DomainEvent {
    @Override public UUID getEventId() { return eventId; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    @Override public long getVersion() { return version; }
}