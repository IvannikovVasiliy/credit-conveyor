package com.neoflex.creditconveyer.application.error.validation;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class ErrorResponseValidation implements Serializable {

    @Serial
    static final long serialVersionUID = 5783645648564864748L;

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