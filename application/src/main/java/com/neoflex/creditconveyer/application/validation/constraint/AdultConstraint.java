package com.neoflex.creditconveyer.application.validation.constraint;

import com.neoflex.creditconveyer.application.validation.validator.AdultValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.Documented;

@Documented
@Constraint(validatedBy = AdultValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AdultConstraint {
    String message() default "Invalid value. Birthday is less than 18 years";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}