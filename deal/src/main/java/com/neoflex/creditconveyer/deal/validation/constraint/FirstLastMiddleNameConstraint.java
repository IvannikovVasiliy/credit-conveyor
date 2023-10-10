package com.neoflex.creditconveyer.deal.validation.constraint;

import com.neoflex.creditconveyer.deal.validation.validator.FirstLastMiddleNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FirstLastMiddleNameValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FirstLastMiddleNameConstraint {
    String message() default "Invalid value. Length should be between 2 and 30. Letters should be latin.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}