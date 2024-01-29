package com.neoflex.creditconveyor.conveyor.error.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErrorResponseValidation {

    public ErrorResponseValidation(List<Violation> violations) {
        this.violations = violations;
    }

    public ErrorResponseValidation() {}

    private List<Violation> violations;
}
