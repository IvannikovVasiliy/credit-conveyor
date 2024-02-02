package com.neoflex.creditconveyor.conveyor.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class DatesUtil {

    public static Integer getYears(LocalDate birthday) {
        log.debug("Input getYears. birthday={}", birthday);

        LocalDate now = LocalDate.now();
        int years = now.getYear() - birthday.getYear();

        if (birthday.getMonthValue() > now.getMonthValue()) {
            years--;
        } else if (birthday.getMonthValue() == now.getMonthValue()) {
            if (birthday.getDayOfMonth() > now.getDayOfMonth()) {
                years--;
            }
        }

        log.debug("Output getYears. birthday={}, years={}", birthday, years);

        return years;
    }
}
