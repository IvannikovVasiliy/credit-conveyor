package com.neoflex.creditconveyer.deal.validation.validator;

import com.neoflex.creditconveyer.deal.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.deal.validation.constraint.AdultConstraint;
import com.neoflex.creditconveyer.utils.DatesUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import com.neoflex.creditconveyer.deal.domain.constant.Constants;

@Slf4j
public class AdultValidator implements ConstraintValidator<AdultConstraint, LocalDate> {

    private DatesUtil datesUtil = new DatesUtil();

    @Override
    public void initialize(AdultConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate birthday, ConstraintValidatorContext constraintValidatorContext) {
        log.debug("Request, validation birthday. birthday={}", birthday);

        if (null == birthday) {
            throw new ResourceNotFoundException("Invalid value. Birthday shouldn't be null");
        }
        Integer years = datesUtil.getYears(birthday);

        log.debug("Response, validation birthday. years={}", years);
        return years >= Constants.AGE_ADULT;
    }
}