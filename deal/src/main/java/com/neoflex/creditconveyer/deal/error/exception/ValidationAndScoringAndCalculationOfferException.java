package com.neoflex.creditconveyer.deal.error.exception;

import com.neoflex.creditconveyer.deal.error.validation.Violation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationAndScoringAndCalculationOfferException extends RuntimeException {

    public ValidationAndScoringAndCalculationOfferException(List<Violation> violations) {
        super();
        this.violations = violations;
    }

    private List<Violation> violations;
}