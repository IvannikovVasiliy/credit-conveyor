package com.neoflex.creditconveyer.deal.validation.constraint;

import com.neoflex.creditconveyer.deal.validation.validator.EmploymentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmploymentValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmploymentConstraint {
    String message() default "Invalid value. Employment is invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}