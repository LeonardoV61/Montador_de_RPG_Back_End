package com.draftstd.hydraforge.adapters.out.projections.adapter;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.ports.out.messaging.SessaoMessagingOutputPort;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PersonagemProjectionAdapter {

    private final SessaoMessagingOutputPort sessaoMessagingOutputPort;

    public PersonagemProjectionAdapter(SessaoMessagingOutputPort sessaoMessagingOutputPort) {
        this.sessaoMessagingOutputPort = sessaoMessagingOutputPort;
    }

    @EventListener
    @Async
    public void onPersonagemEvent(DomainEvent event) {
        String eventName = event.getClass().getSimpleName();

        // Processa apenas eventos relacionados ao Personagem
        if (eventName.contains("Personagem") || eventName.contains("Item") || eventName.contains("Descanso")) {
            // 1. Atualizar tabelas de leitura/consulta (Read Model) se necessário

            // 2. Transmitir atualização via WebSocket para os participantes
            // sessaoMessagingOutputPort.notificarStatusSessao(personagemId, eventName);
        }
    }
}