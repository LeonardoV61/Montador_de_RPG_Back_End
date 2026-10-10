package com.draftstd.hydraforge.domain.service.engine.exceptions;

public class JsonFieldNotFoundException extends RuntimeException {
    public JsonFieldNotFoundException(String campo, String nomeEtapa) {
        super(String.format("%s não foi encontrado no JSON na etapa %s ", campo, nomeEtapa));
    }
}
