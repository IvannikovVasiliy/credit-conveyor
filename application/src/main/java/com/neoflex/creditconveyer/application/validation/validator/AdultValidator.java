package com.neoflex.creditconveyer.application.validation.validator;

import com.neoflex.creditconveyer.application.domain.constant.Constants;
import com.neoflex.creditconveyer.application.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.utils.DatesUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class AdultValidator {

    private DatesUtil datesUtil = new DatesUtil();

    public boolean isValid(LocalDate birthday) {
        log.debug("Request, validation birthday. birthday={}", birthday);

        if (null == birthday) {
            log.error("Invalid value. Birthday shouldn't be null");
            throw new ResourceNotFoundException("Invalid value. Birthday shouldn't be null");
        }
        Integer years = datesUtil.getYears(birthday);

        log.debug("Response, validation birthday. years={}", years);
        return years >= Constants.AGE_ADULT;
    }
}
