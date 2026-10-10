package com.draftstd.hydraforge.adapters.out.projections.adapter;

import com.draftstd.hydraforge.adapters.out.projections.persistence.sessao.SessaoPersistenceAdapter;
import com.draftstd.hydraforge.domain.events.sessao.SessaoCriadaEvent;
import com.draftstd.hydraforge.domain.events.sessao.StatusSessaoAlteradoEvent;
import com.draftstd.hydraforge.ports.out.messaging.SessaoMessagingOutputPort;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SessaoProjectionAdapter {

    private final SessaoPersistenceAdapter sessaoReadModelAdapter;
    private final SessaoMessagingOutputPort messagingOutputPort;

    public SessaoProjectionAdapter(SessaoPersistenceAdapter sessaoReadModelAdapter,
                                 SessaoMessagingOutputPort messagingOutputPort) {
        this.sessaoReadModelAdapter = sessaoReadModelAdapter;
        this.messagingOutputPort = messagingOutputPort;
    }

    @EventListener
    public void onSessaoCriada(SessaoCriadaEvent event) {
        // 1. Atualiza a tabela SQL relacional de leitura
        sessaoReadModelAdapter.criarVisaoLeitura(event);

        // 2. Dispara a notificação via WebSocket
        messagingOutputPort.notificarStatusSessao(event.sessaoId().toString(), "SESSAO_CRIADA");
    }

    @EventListener
    public void onStatusAlterado(StatusSessaoAlteradoEvent event) {
        // 1. Atualiza o status na tabela SQL de leitura
        sessaoReadModelAdapter.atualizarStatusVisao(event);

        // 2. Notifica a mesa em tempo real
        messagingOutputPort.notificarStatusSessao(event.sessaoId().toString(), event.novoStatus().name());
    }
}