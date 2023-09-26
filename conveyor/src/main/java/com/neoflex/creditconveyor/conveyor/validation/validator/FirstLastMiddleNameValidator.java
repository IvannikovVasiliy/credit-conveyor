package com.neoflex.creditconveyor.conveyor.validation.validator;

import com.neoflex.creditconveyor.conveyor.validation.constraint.FirstLastMiddleNameConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstLastMiddleNameValidator implements ConstraintValidator<FirstLastMiddleNameConstraint, String> {
    @Override
    public void initialize(FirstLastMiddleNameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "[a-zA-Z]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        boolean isLatin = matcher.matches();

        return
                str.length() >= 2 &&
                str.length() <= 30 &&
                isLatin;
    }
}
