package com.neoflex.creditconveyor.conveyor.util;

import java.math.BigDecimal;

public class Constants {
    public static final Long APPLICATION_ID = 123456L;

    public static final Integer INSURANCE_CONSTANT_ARGUMENT = 10000;
    public static final Integer INSURANCE_CONSTANT_DENOMINATOR = 1000;

    public static final BigDecimal BASE_RATE = BigDecimal.valueOf(10);
    public static final Integer RATE_SALE_FOR_SALARY_CLIENTS = 1;
    public static final Integer RATE_SALE_FOR_INSURANCE = 5;
    public static final Integer RATE_SALE_FOR_INSURANCE_AND_AGREEMENT_SALARY_TRANSACTION = 6;
    public static final Integer RATE_FOR_SELF_EMPLOYED = 1;
    public static final Integer RATE_FOR_OWNER_BUSINESS = 3;
    public static final Integer RATE_FOR_MID_MANAGER = -2;
    public static final Integer RATE_FOR_TOP_MANAGER = -4;
    public static final Integer RATE_SALE_FOR_FEMALE = -3;
    public static final Integer RATE_SALE_FOR_MALE = -3;
    public static final Integer RATE_FOR_NONBINARY_GENDER = 3;
    public static final Integer RATE_FOR_MARRIED = -3;
    public static final Integer RATE_FOR_DIVORCED = 1;

    public static final Integer COUNT_SALARIES = 20;

    public static final Integer MAX_COUNT_DEPENDENT_AMOUNT = 1;

    public static final Integer AGE_ADULT = 18;
    public static final Integer MIN_AGE = 20;
    public static final Integer MAX_AGE = 60;
    public static final Integer MIN_AGE_SALE_FEMALE = 35;
    public static final Integer MAX_AGE_SALE_FEMALE = 60;
    public static final Integer MIN_AGE_SALE_MALE = 30;
    public static final Integer MAX_AGE_SALE_MALE = 55;

    public static final Integer MIN_VALID_TOTAL_EXPERIENCE = 12;
    public static final Integer MIN_VALID_CURRENT_EXPERIENCE = 3;

    public static final Integer MAX_PERCENT = 100;

    public static final Integer COUNT_MONTHS = 12;

    public static final Integer ACCURACY = 10;
    public static final Integer ACCURACY_DEBT_PAYMENT = 2;

    public static final Integer COUNT_DAYS_IN_LEAP_YEAR = 366;
    public static final Integer COUNT_DAYS_IN_NON_LEAP_YEAR = 365;
}
