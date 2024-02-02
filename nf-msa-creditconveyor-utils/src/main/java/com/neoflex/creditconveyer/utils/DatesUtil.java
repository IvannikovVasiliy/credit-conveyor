package com.neoflex.creditconveyer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class DatesUtil {

    private final Logger LOGGER = LoggerFactory.getLogger(DatesUtil.class);

    public Integer getYears(LocalDate birthday) {
        LOGGER.debug("Input getYears. birthday={}", birthday);

        LocalDate now = LocalDate.now();
        int years = now.getYear() - birthday.getYear();

        if (birthday.getMonthValue() > now.getMonthValue()) {
            years--;
        } else if (birthday.getMonthValue() == now.getMonthValue()) {
            if (birthday.getDayOfMonth() > now.getDayOfMonth()) {
                years--;
            }
        }

        LOGGER.debug("Output getYears. birthday={}, years={}", birthday, years);

        return years;
    }
}