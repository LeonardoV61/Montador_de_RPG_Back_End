package com.draftstd.hydraforge.adapters.out.persistence.personagem.adapter;

import com.draftstd.hydraforge.domain.events.DomainEvent;
import com.draftstd.hydraforge.domain.events.HasSessao;
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

        if (eventName.contains("Personagem") || eventName.contains("Item") || eventName.contains("Descanso")) {
            try {
                // Verifica com elegância e segurança se o evento possui o contrato HasSessao
                if (event instanceof HasSessao eventoComSessao) {
                    if (eventoComSessao.getSessaoId() != null) {
                        sessaoMessagingOutputPort.notificarStatusSessao(eventoComSessao.getSessaoId(), eventName);
                    }
                }
            } catch (Exception ignored) {
                // Log de erro ao transmitir notificação assíncrona
            }
        }
    }
}