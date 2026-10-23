package com.draftstd.hydraforge.domain.validation.exception;

public class TipoEntidadeInvalidoException extends RuntimeException {

    public TipoEntidadeInvalidoException(String tipo) {
        super(
            "O tipo '%s' não existe no schema_entidades deste sistema".formatted(tipo)
        );
    }

}
