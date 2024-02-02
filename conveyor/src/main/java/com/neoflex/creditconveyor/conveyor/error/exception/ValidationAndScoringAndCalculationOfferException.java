package com.neoflex.creditconveyor.conveyor.error.exception;

import com.neoflex.creditconveyor.conveyor.error.validation.Violation;
import lombok.Getter;

import java.io.Serial;
import java.util.List;

@Getter
public class ValidationAndScoringAndCalculationOfferException extends RuntimeException {

    public ValidationAndScoringAndCalculationOfferException(List<Violation> violations) {
        super();
        this.violations = violations;
    }

    private final transient List<Violation> violations;
}
