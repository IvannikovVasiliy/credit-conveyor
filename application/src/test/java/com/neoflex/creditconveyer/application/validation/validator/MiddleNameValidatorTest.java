package com.neoflex.creditconveyer.application.validation.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MiddleNameValidatorTest {

    private final MiddleNameValidator midlleNameValidator = new MiddleNameValidator();

    @Test
    void isValidTest() {
        assertTrue(midlleNameValidator.isValid(null));
        assertFalse(midlleNameValidator.isValid(""));
        assertFalse(midlleNameValidator.isValid("Iv1n"));
        assertFalse(midlleNameValidator.isValid("Иван"));
        assertFalse(midlleNameValidator.isValid("Иvan"));
        assertFalse(midlleNameValidator.isValid("I"));
        assertFalse(midlleNameValidator.isValid("qwertyqwertyqwertyqwertyqwertyqwertyqwerty"));
        assertTrue(midlleNameValidator.isValid("Ivan"));
    }
}