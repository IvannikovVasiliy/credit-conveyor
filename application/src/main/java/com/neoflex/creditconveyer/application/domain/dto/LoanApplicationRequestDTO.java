package com.neoflex.creditconveyer.application.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LoanApplicationRequestDTO {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
}

