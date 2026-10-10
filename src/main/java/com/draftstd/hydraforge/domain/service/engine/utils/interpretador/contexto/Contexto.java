package com.draftstd.hydraforge.domain.service.engine.utils.interpretador.contexto;

import java.util.Optional;

public interface Contexto {
    Optional<Object> get(String caminho);
}
