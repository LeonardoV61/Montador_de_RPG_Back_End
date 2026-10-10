package com.draftstd.hydraforge.adapters.out.messaging;

import com.draftstd.hydraforge.ports.out.messaging.SessaoMessagingOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketMessagingAdapter implements SessaoMessagingOutputPort {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void broadcastSessao(Long idSessao, String tipo, Map<String, Object> payload) {
        messagingTemplate.convertAndSend(
                "/topic/sessao/" + idSessao,
                Map.of("tipo", tipo, "payload", payload)
        );
    }
}