package com.neoflex.creditconveyor.conveyor.validation.constraint;

import com.neoflex.creditconveyor.conveyor.validation.validator.AdultValidator;
import com.neoflex.creditconveyor.conveyor.validation.validator.FirstLastMiddleNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AdultValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AdultConstraint {
    String message() default "Invalid value. Birthday is less than 18 years";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
