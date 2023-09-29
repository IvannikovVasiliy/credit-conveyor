package com.neoflex.creditconveyer.deal.domain.entity;

import com.neoflex.creditconveyer.deal.domain.enumeration.EmploymentPosition;
import com.neoflex.creditconveyer.deal.domain.enumeration.EmploymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "passport")
@Getter
@Setter
public class PassportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passport_id")
    private Long id;

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
