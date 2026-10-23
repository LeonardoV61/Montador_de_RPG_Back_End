package com.draftstd.hydraforge.domain.events.sistema;

import com.draftstd.hydraforge.domain.events.DomainEvent;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record EntidadeInstanciaAlteradaEvent(UUID eventId, String aggregateId, UUID entidadeSistemaId, UUID donoId, UUID campanhaId, UUID sessaoId, String nome, String tipo, String descricao, String urlImagem, Map<String, Object> valoresAtributos, Map<String, Object> customizacoes, String acao, long version, Instant occurredOn) implements DomainEvent {

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