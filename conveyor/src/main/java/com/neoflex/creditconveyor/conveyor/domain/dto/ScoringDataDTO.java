package com.neoflex.creditconveyor.conveyor.domain.dto;

import com.neoflex.creditconveyor.conveyor.domain.enumeration.Gender;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.MartialStatus;
import com.neoflex.creditconveyor.conveyor.validation.constraint.AdultConstraint;
import com.neoflex.creditconveyor.conveyor.validation.constraint.EmploymentConstraint;
import com.neoflex.creditconveyor.conveyor.validation.constraint.FirstLastMiddleNameConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ScoringDataDTO {

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

    @NotNull
    private Gender gender;
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

    private Integer dependentAmount;

    @EmploymentConstraint
    private EmploymentDTO employment;

    private String account;

    @NotNull
    private Boolean isInsuranceEnabled;
    @NotNull
    private Boolean isSalaryClient;
}
