package com.neoflex.creditconveyor.conveyor.validation.validator;

import com.neoflex.creditconveyor.conveyor.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyor.conveyor.util.Constants;
import com.neoflex.creditconveyor.conveyor.util.DatesUtil;
import com.neoflex.creditconveyor.conveyor.validation.constraint.AdultConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class AdultValidator implements ConstraintValidator<AdultConstraint, LocalDate> {

    @Override
    public void initialize(AdultConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate birthday, ConstraintValidatorContext constraintValidatorContext) {
        log.debug("Request, validation birthday. birthday={}", birthday);

        if (null == birthday) {
            log.error("Invalid value. Birthday shouldn't be null");
            throw new ResourceNotFoundException("Invalid value. Birthday shouldn't be null");
        }
        Integer years = DatesUtil.getYears(birthday);

        log.debug("Response, validation birthday. years={}", years);
        return years >= Constants.AGE_ADULT;
    }
}
