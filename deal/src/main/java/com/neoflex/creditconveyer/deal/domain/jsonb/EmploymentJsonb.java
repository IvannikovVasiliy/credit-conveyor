package com.neoflex.creditconveyer.deal.domain.jsonb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neoflex.creditconveyer.deal.domain.enumeration.EmploymentPosition;
import com.neoflex.creditconveyer.deal.domain.enumeration.EmploymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class EmploymentJsonb implements Serializable {

    @JsonProperty("employment_id")
    private UUID employmentId = UUID.randomUUID();

    @Enumerated(value = EnumType.STRING)
    private EmploymentStatus status;

    @Column(name = "employer_inn")
    private String employerInn;

    private BigDecimal salary;
    private EmploymentPosition position;

    @Column(name = "work_experience_total")
    private Integer workExperienceTotal;
    @Column(name = "work_experience_current")
    private Integer workExperienceCurrent;
}
