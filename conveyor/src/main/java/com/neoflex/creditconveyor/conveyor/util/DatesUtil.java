package com.neoflex.creditconveyor.conveyor.util;

import com.neoflex.creditconveyor.conveyor.schedule.PaymentSchedule;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class DatesUtil {

    private DatesUtil() {
        throw new IllegalStateException("Attempt to initialize object of util-class: " + DatesUtil.class.getName());
    }

    public static Integer getYears(LocalDate birthday) {
        log.debug("Input getYears. birthday={}", birthday);

        LocalDate now = LocalDate.now();
        int years = now.getYear() - birthday.getYear();

        if (birthday.getMonthValue() > now.getMonthValue()) {
            years--;
        } else if (birthday.getMonthValue() == now.getMonthValue() && birthday.getDayOfMonth() > now.getDayOfMonth()) {
            years--;
        }

        log.debug("Output getYears. birthday={}, years={}", birthday, years);

        return years;
    }
}
