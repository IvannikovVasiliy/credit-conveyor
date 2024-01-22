package com.neoflex.creditconveyer.deal.domain.dto;

import com.neoflex.creditconveyer.deal.domain.enumeration.EmploymentPosition;
import com.neoflex.creditconveyer.deal.domain.enumeration.EmploymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EmploymentDTO {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}