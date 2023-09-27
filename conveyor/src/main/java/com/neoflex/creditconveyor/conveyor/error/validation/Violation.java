package com.neoflex.creditconveyor.conveyor.error.validation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Violation {

    public Violation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public Violation() {}

    private String fieldName;
    private String message;
}
