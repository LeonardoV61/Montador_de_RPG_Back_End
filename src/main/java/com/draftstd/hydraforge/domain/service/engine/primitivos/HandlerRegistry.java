package com.draftstd.hydraforge.domain.service.engine.primitivos;

import org.springframework.stereotype.Component;

import com.draftstd.hydraforge.domain.service.engine.procedimentos.interfaces.EtapaHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HandlerRegistry {

    private final Map<String, EtapaHandler> handlers;

    public HandlerRegistry(List<EtapaHandler> handlerList) {
        Map<String, EtapaHandler> map = new HashMap<>();
        for (EtapaHandler h : handlerList) {
            String tipo = h.tipoEtapa();
            if (map.containsKey(tipo)) {
                throw new IllegalStateException(
                        "Dois handlers para tipo_etapa '" + tipo + "': " +
                                map.get(tipo).getClass().getSimpleName() + " e " +
                                h.getClass().getSimpleName());
            }
            map.put(tipo, h);
        }
        this.handlers = Collections.unmodifiableMap(map);
    }

    public EtapaHandler get(String tipoEtapa) { return handlers.get(tipoEtapa); }
    public boolean possui(String tipoEtapa)   { return handlers.containsKey(tipoEtapa); }
}