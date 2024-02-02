package com.neoflex.creditconveyer.application.error.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String errorMessage) {
        super(errorMessage);
        this.correlationId = null;
    }

    public ResourceNotFoundException(String errorMessage, String correlationId) {
        super(errorMessage);
        this.correlationId = correlationId;
    }

    private final String correlationId;

    public String getCorrelationId() {
        return correlationId;
    }
}
