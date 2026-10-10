package com.draftstd.hydraforge.adapters.out.projections.adapter;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.ports.out.messaging.SessaoMessagingOutputPort;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BatalhaProjectionAdapter {

    private final SessaoMessagingOutputPort sessaoMessagingOutputPort;

    public BatalhaProjectionAdapter(SessaoMessagingOutputPort sessaoMessagingOutputPort) {
        this.sessaoMessagingOutputPort = sessaoMessagingOutputPort;
    }

    @EventListener
    public void onEvent(DomainEvent event) {
        // Escuta os eventos despachados no barramento interno do Spring
        // Ex: BatalhaIniciadaEvent, TurnoAlteradoEvent, BatalhaEncerradaEvent
        
        // Exemplo: Notificar a mesa via WebSocket sobre atualizações no combate
        if (event.getClass().getSimpleName().contains("Batalha")) {
            // Dispara para os clientes conectados no canal da sessão/batalha
            sessaoMessagingOutputPort.notificarStatusSessao("batalha-id", event.getClass().getSimpleName());
        }
    }
}