package com.neoflex.creditconveyer.application.error.exception;

public class ConnectionRefusedException extends RuntimeException {

    public ConnectionRefusedException(String message) {
        super(message);
    }
}
