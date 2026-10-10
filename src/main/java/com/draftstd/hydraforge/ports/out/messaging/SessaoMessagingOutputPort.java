package com.draftstd.hydraforge.ports.out.messaging;

import java.util.Map;

public interface SessaoMessagingOutputPort {
    void broadcastSessao(Long idSessao, String tipo, Map<String, Object> payload);
}