package com.draftstd.hydraforge.domain.events.personagem;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record PersonagemCriadoEvent(UUID eventId, String aggregateId, UUID usuarioId, UUID campanhaId, UUID instanciaId, String nome, String historia, String aparencia, String urlImagem, String notasJogador, Map<String, Object> atributos, long version, Instant occurredOn, UUID sessaoId) implements DomainEvent {
    @Override public UUID getEventId() { return eventId; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    @Override public long getVersion() { return version; }

}