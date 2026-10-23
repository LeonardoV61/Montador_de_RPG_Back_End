package com.draftstd.hydraforge.ports.out.messaging;

import java.util.Map;
import java.util.UUID;

public interface SessaoMessagingOutputPort {
    void broadcastSessao(UUID idSessao, String tipo, Map<String, Object> payload);
    void notificarStatusSessao(UUID idSessao, String tipoEvento);
}