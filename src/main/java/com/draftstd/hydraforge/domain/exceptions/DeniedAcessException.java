package com.draftstd.hydraforge.domain.exceptions;

public class DeniedAcessException extends RuntimeException {
    public DeniedAcessException(String message) {
        super(message);
    }
}
