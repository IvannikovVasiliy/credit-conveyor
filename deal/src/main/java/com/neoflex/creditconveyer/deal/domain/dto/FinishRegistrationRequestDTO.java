package com.neoflex.creditconveyer.deal.domain.dto;

import com.example.credit.application.model.EmploymentDTO;
import com.neoflex.creditconveyer.deal.domain.enumeration.MartialStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class FinishRegistrationRequestDTO {
    private BigDecimal gender;
    private MartialStatus maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private EmploymentDTO employment;
    private String account;
}
