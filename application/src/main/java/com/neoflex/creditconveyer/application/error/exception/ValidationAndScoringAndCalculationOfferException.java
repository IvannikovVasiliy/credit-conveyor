package com.neoflex.creditconveyer.application.error.exception;

import com.neoflex.creditconveyer.application.error.validation.ErrorResponseValidation;
import lombok.Getter;

@Getter
public class ValidationAndScoringAndCalculationOfferException extends RuntimeException {

    public ValidationAndScoringAndCalculationOfferException(ErrorResponseValidation violations) {
        super();
        this.violations = violations;
    }

    private ErrorResponseValidation violations;
}