package com.neoflex.creditconveyer.deal.error.exception;

public class ErrorSesCodeException extends RuntimeException {

    public ErrorSesCodeException() {
    }

    public ErrorSesCodeException(String message) {
        super(message);
    }

    public ErrorSesCodeException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return "Exception with SES code: " + getMessage();
    }

}