package com.draftstd.hydraforge.adapters.out.messaging;

import com.draftstd.hydraforge.ports.out.messaging.SessaoMessagingOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WebSocketMessagingAdapter implements SessaoMessagingOutputPort {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void broadcastSessao(UUID idSessao, String tipo, Map<String, Object> payload) {
        Map<String, Object> mensagem = new HashMap<>();
        mensagem.put("tipo", tipo);
        mensagem.put("payload", payload);

        messagingTemplate.convertAndSend("/topic/sessao/" + idSessao, (Object) mensagem);
    }

    @Override
    public void notificarStatusSessao(UUID idSessao, String status) {
        Map<String, Object> mensagem = new HashMap<>();
        mensagem.put("tipo", "STATUS_SESSAO");
        mensagem.put("status", status);

        messagingTemplate.convertAndSend("/topic/sessao/" + idSessao + "/status", (Object) mensagem);
    }
}