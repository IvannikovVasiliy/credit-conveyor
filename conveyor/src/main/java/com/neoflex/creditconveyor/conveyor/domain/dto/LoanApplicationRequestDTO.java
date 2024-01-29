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

    public LoanApplicationRequestDTO(BigDecimal amount, Integer term,
                                     String firstName, String lastName, String middleName,
                                     String email, LocalDate birthdate, String passportSeries, String passportNumber) {
        this.amount = amount;
        this.term = term;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.birthdate = birthdate;
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
    }

    public LoanApplicationRequestDTO(BigDecimal amount, Integer term, String firstName, String lastName,
                                     LocalDate birthdate, String passportSeries, String passportNumber) {
        this.amount = amount;
        this.term = term;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
    }

    public LoanApplicationRequestDTO() {
    }

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
    @Size(min = 4, max = 4)
    private String passportSeries;
    @NotNull
    @Size(min = 6, max = 6)
    private String passportNumber;
}
