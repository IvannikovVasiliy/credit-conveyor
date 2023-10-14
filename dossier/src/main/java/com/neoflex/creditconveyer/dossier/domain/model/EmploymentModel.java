package com.neoflex.creditconveyer.dossier.domain.model;

import com.neoflex.creditconveyer.dossier.domain.enumeration.EmploymentPosition;
import com.neoflex.creditconveyer.dossier.domain.enumeration.EmploymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EmploymentModel {
    private EmploymentStatus status;
    private String employerInn;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}