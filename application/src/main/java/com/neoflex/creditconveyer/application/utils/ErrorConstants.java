package com.neoflex.creditconveyer.application.utils;

public class ErrorConstants {

    private ErrorConstants() {
        throw new IllegalStateException("Attempt to initialize object of util-class: " + ErrorConstants.class.getName());
    }

    public static final Integer DEFAULT_ERROR_CODE = -1;

    public static final Integer BAD_REQUEST = 400;
    public static final Integer NOT_FOUND = 404;
    public static final Integer INTERNAL_SERVER_ERROR = 500;
}
