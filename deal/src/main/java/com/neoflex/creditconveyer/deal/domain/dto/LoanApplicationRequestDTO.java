package com.neoflex.creditconveyer.deal.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanApplicationRequestDTO   {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String email;
    private String birthdate;
    private String passportSeries;
    private String passportNumber;
}

