package com.neoflex.creditconveyer.application.error.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
