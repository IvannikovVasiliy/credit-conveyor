package com.neoflex.creditconveyer.application.validation.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FirstNameValidatorTest {

    private final FirstNameValidator firstNameValidator = new FirstNameValidator();

    @Test
    void isValidTest() {
        assertFalse(firstNameValidator.isValid(null));
        assertFalse(firstNameValidator.isValid(""));
        assertFalse(firstNameValidator.isValid("Iv1n"));
        assertFalse(firstNameValidator.isValid("Иван"));
        assertFalse(firstNameValidator.isValid("Иvan"));
        assertFalse(firstNameValidator.isValid("I"));
        assertFalse(firstNameValidator.isValid("qwertyqwertyqwertyqwertyqwertyqwertyqwerty"));
        assertTrue(firstNameValidator.isValid("Ivan"));
    }
}