package com.neoflex.creditconveyor.conveyor.util;

import com.neoflex.creditconveyor.conveyor.schedule.PaymentSchedule;

public class StatusConstants {

    private StatusConstants() {
        throw new IllegalStateException("Attempt to initialize object of util-class: " + StatusConstants.class.getName());
    }

    public static final Integer DEFAULT_ERROR_CODE = -1;
}
