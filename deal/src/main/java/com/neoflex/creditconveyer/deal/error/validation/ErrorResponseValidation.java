package com.neoflex.creditconveyer.deal.error.validation;

import java.util.List;

public class ErrorResponseValidation {

    public ErrorResponseValidation(List<Violation> violations) {
        this.violations = violations;
    }

    public ErrorResponseValidation() {
    }

    private List<Violation> violations;

    public List<Violation> getViolations() {
        return violations;
    }

    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }
}
