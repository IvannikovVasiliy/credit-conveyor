package com.neoflex.creditconveyor.conveyor.validation.validator;

import com.neoflex.creditconveyor.conveyor.domain.constants.Constants;
import com.neoflex.creditconveyor.conveyor.domain.dto.EmploymentDTO;
import com.neoflex.creditconveyor.conveyor.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyor.conveyor.utils.DatesUtil;
import com.neoflex.creditconveyor.conveyor.validation.constraint.EmploymentConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmploymentValidator implements ConstraintValidator<EmploymentConstraint, EmploymentDTO> {

    @Override
    public void initialize(EmploymentConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(EmploymentDTO employment, ConstraintValidatorContext constraintValidatorContext) {
        log.debug("Request, validation EmploymentDTO");

        if (null == employment) {
            throw new ResourceNotFoundException("Invalid value. Employment shouldn't be null");
        }
        if (null == employment.getWorkExperienceTotal()) {
            throw new ResourceNotFoundException("Invalid value. workExperienceTotal shouldn't be null");
        }
        if (null == employment.getWorkExperienceCurrent()) {
            throw new ResourceNotFoundException("Invalid value. workExperienceCurrent shouldn't be null");
        }

        log.debug("Response, validation EmploymentDTO for null is finished");
        return true;
    }
}
