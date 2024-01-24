package com.neoflex.creditconveyer.deal.error.exception;

public class ConnectionRefusedException extends RuntimeException {

    public ConnectionRefusedException(String message) {
        super(message);
    }
}