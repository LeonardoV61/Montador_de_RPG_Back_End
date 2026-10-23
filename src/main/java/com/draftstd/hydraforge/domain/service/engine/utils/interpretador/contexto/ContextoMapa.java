package com.draftstd.hydraforge.domain.service.engine.utils.interpretador.contexto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ContextoMapa implements Contexto {

    private final Map<String, Object> raiz;

    public ContextoMapa(Map<String, Object> raiz) {
        this.raiz = raiz;
    }

    @Override
    public Optional<Object> get(String caminho) {
        if (raiz == null || caminho == null || caminho.isBlank()) {
            return Optional.empty();
        }

        String[] partes = caminho.split("\\.");
        Object atual = raiz;

        for (String parte : partes) {
            if (atual instanceof Map<?, ?> map) {
                atual = map.get(parte);
            } else if (atual instanceof List<?> list) {
                try {
                    int index = Integer.parseInt(parte);
                    if (index >= 0 && index < list.size()) {
                        atual = list.get(index);
                    } else {
                        return Optional.empty();
                    }
                } catch (NumberFormatException e) {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }

            if (atual == null) {
                return Optional.empty();
            }
        }

        return Optional.of(atual);
    }
}