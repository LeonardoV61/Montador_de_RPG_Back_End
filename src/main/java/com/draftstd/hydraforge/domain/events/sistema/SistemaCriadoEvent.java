package com.draftstd.hydraforge.domain.events.sistema;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record SistemaCriadoEvent(
    UUID eventId,
    String aggregateId,
    UUID usuarioCriadorId,
    UUID sistemaPaiId,
    String nome,
    String descricao,
    String urlImagem,
    Map<String, Object> configuracao,
    Map<String, Object> schemaAtributos,
    Map<String, Object> schemaEntidades,
    Map<String, Object> schemaResolucoes,
    boolean eOficial,
    long version,
    Instant occurredOn,
    UUID sessaoId
) implements DomainEvent {
    @Override public UUID getEventId() { return eventId; }
    @Override public String getAggregateId() { return aggregateId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    @Override public long getVersion() { return version; }
}