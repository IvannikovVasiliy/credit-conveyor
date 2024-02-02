package com.neoflex.creditconveyer.application.utils;

public class HttpConfig {

    private HttpConfig() {
        throw new IllegalStateException("Attempt to initialize object of util-class: " + HttpConfig.class.getName());
    }

    public static final String CORRELATION_ID_HEADER_CONFIG = "Correlation-Id";
}
