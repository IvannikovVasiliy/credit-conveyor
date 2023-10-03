package com.neoflex.creditconveyer.deal.validation.validator;

import com.neoflex.creditconveyer.deal.domain.constant.Constants;
import com.neoflex.creditconveyer.deal.domain.dto.EmploymentDTO;
import com.neoflex.creditconveyer.deal.error.exception.BadRequestException;
import com.neoflex.creditconveyer.deal.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.deal.validation.constraint.EmploymentConstraint;
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
            log.error("Invalid value. Employment shouldn't be null");
            throw new ResourceNotFoundException("Invalid value. Employment shouldn't be null");
        }
        if (null == employment.getWorkExperienceTotal()) {
            log.error("Invalid value. workExperienceTotal shouldn't be null");
            throw new ResourceNotFoundException("Invalid value. workExperienceTotal shouldn't be null");
        }
        if (null == employment.getWorkExperienceCurrent()) {
            log.error("Invalid value. workExperienceCurrent shouldn't be null");
            throw new ResourceNotFoundException("Invalid value. workExperienceCurrent shouldn't be null");
        }

        if (employment.getWorkExperienceTotal() < Constants.MIN_VALID_TOTAL_EXPERIENCE) {
            log.error("Invalid value. Total experience might not be less than {} months.", Constants.MIN_VALID_TOTAL_EXPERIENCE);
            throw new BadRequestException(String.format("Invalid value. Total experience might not be less than %d months.", Constants.MIN_VALID_TOTAL_EXPERIENCE));
        }
        if (employment.getWorkExperienceCurrent() < Constants.MIN_VALID_CURRENT_EXPERIENCE) {
            log.error("Invalid value. Current experience might not be less than {} months.", Constants.MIN_VALID_CURRENT_EXPERIENCE);
            throw new BadRequestException(String.format("Invalid value. Current experience might not be less than %d months.", Constants.MIN_VALID_CURRENT_EXPERIENCE));
        }

        log.debug("Response, validation EmploymentDTO for null is finished");
        return true;
    }
}