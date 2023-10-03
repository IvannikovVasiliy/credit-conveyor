package com.neoflex.creditconveyer.deal.domain.dto;

import com.neoflex.creditconveyer.deal.domain.enumeration.Gender;
import com.neoflex.creditconveyer.deal.domain.enumeration.MartialStatus;
import com.neoflex.creditconveyer.deal.validation.constraint.AdultConstraint;
import com.neoflex.creditconveyer.deal.validation.constraint.EmploymentConstraint;
import com.neoflex.creditconveyer.deal.validation.constraint.FirstLastMiddleNameConstraint;
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
