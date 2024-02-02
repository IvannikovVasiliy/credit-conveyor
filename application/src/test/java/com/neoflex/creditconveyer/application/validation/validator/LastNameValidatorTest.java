package com.neoflex.creditconveyer.application.validation.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LastNameValidatorTest {

    private final LastNameValidator lastNameValidator = new LastNameValidator();

    @Test
    void isValidTest() {
        assertFalse(lastNameValidator.isValid(null));
        assertFalse(lastNameValidator.isValid(""));
        assertFalse(lastNameValidator.isValid("Iv1n"));
        assertFalse(lastNameValidator.isValid("Иван"));
        assertFalse(lastNameValidator.isValid("Иvan"));
        assertFalse(lastNameValidator.isValid("I"));
        assertFalse(lastNameValidator.isValid("qwertyqwertyqwertyqwertyqwertyqwertyqwerty"));
        assertTrue(lastNameValidator.isValid("Ivan"));
    }
}