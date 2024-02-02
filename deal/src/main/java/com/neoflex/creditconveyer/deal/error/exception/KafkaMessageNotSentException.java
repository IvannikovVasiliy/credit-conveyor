package com.neoflex.creditconveyer.deal.error.exception;

public class KafkaMessageNotSentException extends RuntimeException {

    public KafkaMessageNotSentException(String message) {
        super(message);
    }
}
