package com.draftstd.hydraforge.adapters.out.projections.adapter;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.ports.out.messaging.SessaoMessagingOutputPort;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class BatalhaProjectionAdapter {

    private final SessaoMessagingOutputPort sessaoMessagingOutputPort;

    public BatalhaProjectionAdapter(SessaoMessagingOutputPort sessaoMessagingOutputPort) {
        this.sessaoMessagingOutputPort = sessaoMessagingOutputPort;
    }

    @EventListener
    public void onEvent(DomainEvent event) {
        if (event.getClass().getSimpleName().contains("Batalha")) {
            // Conversão segura do aggregateId para UUID e uso do método correto broadcastSessao
            UUID uuidAgregado = UUID.fromString(event.getAggregateId());
            sessaoMessagingOutputPort.broadcastSessao(
                    uuidAgregado,
                    event.getClass().getSimpleName(),
                    Map.of("eventoId", event.getEventId().toString(), "versao", event.getVersion())
            );
        }
    }
}