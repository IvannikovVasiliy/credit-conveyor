package com.neoflex.creditconveyer.deal.error.exception;

public class ApplicationIsPreapprovalException extends RuntimeException {

    public ApplicationIsPreapprovalException() {
    }

    public ApplicationIsPreapprovalException(String message) {
        super(message);
    }

    public ApplicationIsPreapprovalException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return "BadRequestException: "+getMessage();
    }
}
