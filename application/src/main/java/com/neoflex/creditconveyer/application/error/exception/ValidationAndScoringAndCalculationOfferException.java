package com.neoflex.creditconveyer.application.error.exception;

import com.neoflex.creditconveyer.application.error.validation.Violation;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationAndScoringAndCalculationOfferException extends RuntimeException {

    public ValidationAndScoringAndCalculationOfferException(List<Violation> violations) {
        super();
        this.violations = violations;
    }

    private List<Violation> violations;
}