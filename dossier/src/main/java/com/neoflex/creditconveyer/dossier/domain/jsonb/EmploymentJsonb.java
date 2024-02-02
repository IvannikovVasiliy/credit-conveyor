package com.neoflex.creditconveyer.dossier.domain.jsonb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neoflex.creditconveyer.dossier.domain.enumeration.EmploymentPosition;
import com.neoflex.creditconveyer.dossier.domain.enumeration.EmploymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class EmploymentJsonb {

    @JsonProperty("employment_id")
    private UUID employmentId;
    private EmploymentStatus status;
    private String employerInn;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}