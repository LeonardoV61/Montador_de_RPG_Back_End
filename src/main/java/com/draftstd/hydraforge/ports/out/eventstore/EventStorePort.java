package com.draftstd.hydraforge.ports.out.eventstore;

import com.draftstd.hydraforge.domain.events.DomainEvent;

import java.util.List;
import java.util.UUID;

public interface EventStorePort {
    void salvarEventos(String aggregateId, List<DomainEvent> eventos, long versaoEsperada);
    List<DomainEvent> carregarEventos(String aggregateId);
    List<DomainEvent> carregarEventosPorSessao(UUID sessaoId);
}