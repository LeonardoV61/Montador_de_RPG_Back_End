package com.draftstd.hydraforge.domain.service.engine.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> entityClass, Object id) {
        super(String.format("%s with ID '%s' was not found", entityClass.getSimpleName(), id));
    }

    public EntityNotFoundException(Class<?> entityClass, String fieldName, Object value) {
        super(String.format("%s with %s '%s' was not found",
                entityClass.getSimpleName(), fieldName, value));
    }
}
