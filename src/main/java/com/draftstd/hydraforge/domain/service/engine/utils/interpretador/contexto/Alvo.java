package com.draftstd.hydraforge.domain.service.engine.utils.interpretador.contexto;

import java.util.UUID;

public record Alvo(String tipoEntidade, UUID id) {
    public Alvo(String tipoEntidade, Object id) {
        this(tipoEntidade, comoUuid(id));
    }

    private static UUID comoUuid(Object id) {
        if (id instanceof UUID u) return u;
        if (id instanceof String s && !s.isBlank()) return UUID.fromString(s);
        return null;
    }

    @Override
    public String toString() {
        return tipoEntidade + ":" + id;
    }
}