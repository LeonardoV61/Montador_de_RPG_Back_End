package com.draftstd.hydraforge.domain.service.engine.utils.interpretador.contexto;

public record Alvo(String tipoEntidade, Object id) {
    @Override
    public String toString() {
        return tipoEntidade + ":" + id;
    }
}