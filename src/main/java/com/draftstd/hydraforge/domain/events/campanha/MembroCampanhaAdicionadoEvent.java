package com.draftstd.hydraforge.domain.events.campanha;

import com.draftstd.hydraforge.domain.enums.PapeisUsuario;
import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.domain.enums.StatusMembroCampanha;

import java.time.Instant;
import java.util.UUID;

public record MembroCampanhaAdicionadoEvent(
    UUID eventId,
    String aggregateId,
    Instant occurredOn,
    long version,
    UUID sessaoId,
    UUID usuarioId,
    PapeisUsuario papel,
    StatusMembroCampanha statusMembro
) implements DomainEvent {

    public MembroCampanhaAdicionadoEvent(UUID campanhaId, UUID usuarioId, PapeisUsuario papel, StatusMembroCampanha statusMembro, long version, UUID sessaoId) {
        this(
            UUID.randomUUID(),
            campanhaId.toString(),
            Instant.now(),
            version,
            sessaoId != null ? sessaoId : campanhaId,
            usuarioId,
            papel,
            statusMembro
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