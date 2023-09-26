package com.neoflex.creditconveyor.conveyor.domain.dto;

import com.neoflex.creditconveyor.conveyor.validation.constraint.AdultConstraint;
import com.neoflex.creditconveyor.conveyor.validation.constraint.FirstLastMiddleNameConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LoanApplicationRequestDTO {
    @NotNull
    @Min(10000)
    private BigDecimal amount;
    @NotNull
    @Min(6)
    private Integer term;

    @NotNull
    @FirstLastMiddleNameConstraint
    private String firstName;
    @NotNull
    @FirstLastMiddleNameConstraint
    private String lastName;
    @FirstLastMiddleNameConstraint
    private String middleName;

    @Email(regexp = "[\\w\\.]{2,50}@[\\w\\.]{2,20}")
    private String email;

    @AdultConstraint
    private LocalDate birthdate;

    @NotNull
    @Size(min = 4)
    private String passportSeries;
    @NotNull
    @Size(min = 6)
    private String passportNumber;
}
