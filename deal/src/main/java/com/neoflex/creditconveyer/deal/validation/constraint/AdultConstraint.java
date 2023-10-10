package com.neoflex.creditconveyer.deal.validation.constraint;

import com.neoflex.creditconveyer.deal.validation.validator.AdultValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AdultValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AdultConstraint {
    String message() default "Invalid value. Birthdate is less than 18 years";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}