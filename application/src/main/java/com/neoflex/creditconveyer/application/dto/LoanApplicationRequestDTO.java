package com.neoflex.creditconveyer.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LoanApplicationRequestDTO {
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Integer term;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String middleName;
    private String email;
    private LocalDate birthdate;
    @NotNull(message = "Номер паспорта не должен быть пуст")
    private String passportSeries;
    @NotNull
    private String passportNumber;
}
