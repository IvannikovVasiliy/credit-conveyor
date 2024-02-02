package com.neoflex.creditconveyer.application.utils;

import java.math.BigDecimal;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Attempt to initialize object of util-class: " + Constants.class.getName());
    }

    public static final Integer AGE_ADULT = 18;
    public static final BigDecimal MIN_AMOUNT_CREDIT = BigDecimal.valueOf(10000);
    public static final Integer MIN_TERM = 6;
    public static final Integer LENGTH_PASSPORT_SERIES = 4;
    public static final Integer LENGTH_PASSPORT_NUMBER = 6;
}
