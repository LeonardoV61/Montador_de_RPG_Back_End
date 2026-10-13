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
        // Criando o mapa explicitamente tipado para evitar erros de inferência do compilador
        Map<String, Object> mensagem = new HashMap<>();
        mensagem.put("tipo", tipo);
        mensagem.put("payload", payload);

        messagingTemplate.convertAndSend("/topic/sessao/" + idSessao, (Object) mensagem);
    }
}