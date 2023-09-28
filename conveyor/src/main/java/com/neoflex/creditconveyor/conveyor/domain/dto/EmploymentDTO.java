package com.neoflex.creditconveyor.conveyor.domain.dto;

import com.neoflex.creditconveyor.conveyor.domain.enumeration.EmploymentStatus;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.Position;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EmploymentDTO {

    public EmploymentDTO(Integer workExperienceTotal, Integer workExperienceCurrent) {
        this.workExperienceTotal = workExperienceTotal;
        this.workExperienceCurrent = workExperienceCurrent;
    }

    public EmploymentDTO() {
    }

    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
