package com.neoflex.creditconveyer.deal.error.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String errorMessage) {
        super(errorMessage);
    }
    public ResourceNotFoundException(String errorMessage, String correlationId) {
        super(errorMessage);
        this.correlationId = correlationId;
    }

    private String correlationId;

    public String getCorrelationId() {
        return correlationId;
    }
}
