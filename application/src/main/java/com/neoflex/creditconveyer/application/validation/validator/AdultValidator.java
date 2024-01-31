package com.neoflex.creditconveyer.application.validation.validator;

import com.neoflex.creditconveyer.application.domain.constant.Constants;
import com.neoflex.creditconveyer.application.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.application.utils.DatesUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class AdultValidator {

    private DatesUtil datesUtil = new DatesUtil();

    public boolean isValid(LocalDate birthdate) {
        log.debug("Request, validation birthdate. birthdate={}", birthdate);

        if (null == birthdate) {
            log.error("Invalid value. Birthdate shouldn't be null");
            throw new ResourceNotFoundException("Invalid value. Birthdate shouldn't be null");
        }
        Integer years = datesUtil.getYears(birthdate);

        log.debug("Response, validation birthdate. years={}", years);
        return years >= Constants.AGE_ADULT;
    }
}
