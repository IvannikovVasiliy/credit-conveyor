package com.neoflex.creditconveyor.conveyor.validation.validator;

import com.neoflex.creditconveyor.conveyor.error.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class AdultValidatorTest {

    private final AdultValidator adultValidator = new AdultValidator();
    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @Test
    void isValidTest() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> adultValidator.isValid(null, constraintValidatorContext)
        );
    }
}