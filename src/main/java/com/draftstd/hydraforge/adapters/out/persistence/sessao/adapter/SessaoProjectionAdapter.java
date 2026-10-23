package com.draftstd.hydraforge.adapters.out.persistence.sessao.adapter;

import com.draftstd.hydraforge.domain.events.sessao.SessaoCriadaEvent;
import com.draftstd.hydraforge.domain.events.sessao.StatusSessaoAlteradoEvent;
import com.draftstd.hydraforge.ports.out.messaging.SessaoMessagingOutputPort;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

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

        // 2. Dispara a notificação via WebSocket usando o contrato correto
        messagingOutputPort.broadcastSessao(
                event.sessaoId(),
                "SESSAO_CRIADA",
                Map.of("titulo", event.titulo(), "campanhaId", event.campanhaId().toString())
        );
    }

    @EventListener
    public void onStatusAlterado(StatusSessaoAlteradoEvent event) {
        // 1. Atualiza o status na tabela SQL de leitura
        sessaoReadModelAdapter.atualizarStatusVisao(event);

        // 2. Notifica a mesa em tempo real usando o contrato correto
        messagingOutputPort.broadcastSessao(
                event.sessaoId(),
                "STATUS_SESSAO_ALTERADO",
                Map.of("novoStatus", event.novoStatus().name())
        );
    }
}