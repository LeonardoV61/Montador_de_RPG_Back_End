package com.draftstd.hydraforge.domain.events.sessao;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record SessaoCriadaEvent(
    UUID eventId,
    String aggregateId,
    Instant occurredOn,
    long version,
    String nome,
    UUID campanhaId,
    UUID mestreId
) implements DomainEvent {
    public SessaoCriadaEvent(UUID sessaoId, String nome, UUID campanhaId, UUID mestreId) {
        this(UUID.randomUUID(), sessaoId.toString(), Instant.now(), 1, nome, campanhaId, mestreId);
    }

    @Override public UUID getEventId() { return eventId; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    @Override public long getVersion() { return version; }
}