package com.neoflex.creditconveyor.conveyor.validation.validator;

import com.neoflex.creditconveyor.conveyor.domain.dto.EmploymentDTO;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.EmploymentPosition;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.EmploymentStatus;
import com.neoflex.creditconveyor.conveyor.error.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EmploymentValidatorTest {

    private final EmploymentValidator employmentValidator = new EmploymentValidator();
    @Mock
    ConstraintValidatorContext constraintValidatorContext;

    @Test
    void isValidTest() {
        EmploymentDTO invalidEmploymentStatus = EmploymentDTO
                .builder()
                .employmentStatus(null)
                .position(EmploymentPosition.WORKER)
                .salary(BigDecimal.valueOf(1000))
                .workExperienceTotal(13)
                .workExperienceCurrent(4)
                .build();
        EmploymentDTO invalidTotalExperienceEmployment = EmploymentDTO
                .builder()
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .position(EmploymentPosition.WORKER)
                .salary(BigDecimal.valueOf(1000))
                .workExperienceTotal(null)
                .workExperienceCurrent(4)
                .build();
        EmploymentDTO invalidCurrentExperienceEmployment = EmploymentDTO
                .builder()
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .position(EmploymentPosition.WORKER)
                .salary(BigDecimal.valueOf(1000))
                .workExperienceTotal(13)
                .workExperienceCurrent(null)
                .build();
        EmploymentDTO validEmployment = EmploymentDTO
                .builder()
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .position(EmploymentPosition.WORKER)
                .salary(BigDecimal.valueOf(1000))
                .workExperienceTotal(13)
                .workExperienceCurrent(4)
                .build();

        assertThrows(
                ResourceNotFoundException.class,
                () -> employmentValidator.isValid(null, constraintValidatorContext)
        );
        assertThrows(
                ResourceNotFoundException.class,
                () -> employmentValidator.isValid(invalidEmploymentStatus, constraintValidatorContext)
        );
        assertThrows(
                ResourceNotFoundException.class,
                () -> employmentValidator.isValid(invalidTotalExperienceEmployment, constraintValidatorContext)
        );
        assertThrows(
                ResourceNotFoundException.class,
                () -> employmentValidator.isValid(invalidCurrentExperienceEmployment, constraintValidatorContext)
        );
        assertTrue(employmentValidator.isValid(validEmployment, constraintValidatorContext));
    }
}