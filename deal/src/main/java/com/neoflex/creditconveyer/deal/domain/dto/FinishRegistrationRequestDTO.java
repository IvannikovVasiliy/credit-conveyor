package com.neoflex.creditconveyer.deal.domain.dto;

import com.neoflex.creditconveyer.deal.domain.enumeration.Gender;
import com.neoflex.creditconveyer.deal.domain.enumeration.MartialStatus;
import com.neoflex.creditconveyer.deal.validation.constraint.EmploymentConstraint;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FinishRegistrationRequestDTO {
    private Gender gender;
    private MartialStatus maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    @EmploymentConstraint
    private EmploymentDTO employment;
    private String account;
}
