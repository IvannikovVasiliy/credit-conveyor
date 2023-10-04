package com.neoflex.creditconveyer.deal.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanOfferDTO {
    @NotNull
    private Long applicationId;

    @NotNull
    @Min(10000)
    private BigDecimal requestedAmount;
    @NotNull
    @Min(10000)
    private BigDecimal totalAmount;

    @NotNull
    @Min(6)
    private Integer term;

    @NotNull
    private BigDecimal monthlyPayment;
    @NotNull
    private BigDecimal rate;

    @NotNull
    private Boolean isInsuranceEnabled;
    @NotNull
    private Boolean isSalaryClient;
}

