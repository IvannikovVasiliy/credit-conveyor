package com.neoflex.creditconveyor.conveyor.domain.dto;

import com.neoflex.creditconveyor.conveyor.domain.enumeration.Gender;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.MartialStatus;
import com.neoflex.creditconveyor.conveyor.validation.constraint.AdultConstraint;
import com.neoflex.creditconveyor.conveyor.validation.constraint.EmploymentConstraint;
import com.neoflex.creditconveyor.conveyor.validation.constraint.FirstLastMiddleNameConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ScoringDataDTO {
    public ScoringDataDTO(BigDecimal amount, Integer term, String firstName, String lastName, LocalDate birthdate,
                          String passportSeries, String passportNumber, MartialStatus martialStatus,
                          Integer dependentAmount, EmploymentDTO employment) {
        this.amount = amount;
        this.term = term;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
        this.martialStatus = martialStatus;
        this.dependentAmount = dependentAmount;
        this.employment = employment;
    }

    public ScoringDataDTO() {
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

    private Gender gender;

    @AdultConstraint
    private LocalDate birthdate;

    @NotNull
    @Size(min = 4, max = 4)
    private String passportSeries;
    @NotNull
    @Size(min = 6, max = 6)
    private String passportNumber;

    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private MartialStatus martialStatus;

    @NotNull
    private Integer dependentAmount;

    @EmploymentConstraint
    private EmploymentDTO employment;

    private String account;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
