package com.neoflex.creditconveyor.conveyor.validation.validator;

import com.neoflex.creditconveyor.conveyor.validation.constraint.FirstLastMiddleNameConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class AdultValidator implements ConstraintValidator<FirstLastMiddleNameConstraint, LocalDate> {

    @Override
    public void initialize(FirstLastMiddleNameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate now = LocalDate.now();
        LocalDate birthday = LocalDate.of(2001, 9, 20);

        int years = now.getYear() - birthday.getYear();

        if (birthday.getMonthValue() > now.getMonthValue()) {
            years--;
        } else if (birthday.getMonthValue() == now.getMonthValue()) {
            if (birthday.getDayOfMonth() > now.getDayOfMonth()) {
                years--;
            }
        }

        return years >= 18;
    }
}
