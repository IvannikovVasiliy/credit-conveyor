package com.neoflex.creditconveyor.conveyor.validation.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FirstLastMiddleNameValidatorTest {

    private final FirstLastMiddleNameValidator validator = new FirstLastMiddleNameValidator();
    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @Test
    public void isValidTest() {
        String shortStr = "a";
        String russianStr = "qwerty русский текст";
        String validStr = "valid";

        assertFalse(validator.isValid(shortStr, constraintValidatorContext));
        assertFalse(validator.isValid(russianStr, constraintValidatorContext));
        assertTrue(validator.isValid(validStr, constraintValidatorContext));
    }
}