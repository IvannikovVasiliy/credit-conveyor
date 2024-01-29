package com.neoflex.creditconveyer.application.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
public class LoanOfferDTO {

    public LoanOfferDTO(Long applicationId, BigDecimal requestedAmount, BigDecimal totalAmount, Integer term, BigDecimal monthlyPayment, BigDecimal rate, Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        this.applicationId = applicationId;
        this.requestedAmount = requestedAmount;
        this.totalAmount = totalAmount;
        this.term = term;
        this.monthlyPayment = monthlyPayment;
        this.rate = rate;
        this.isInsuranceEnabled = isInsuranceEnabled;
        this.isSalaryClient = isSalaryClient;
    }

    public LoanOfferDTO() {
    }

    @NotNull
    private Long applicationId;

    @Min(10000)
    @NotNull
    private BigDecimal requestedAmount;
    @Min(10000)
    @NotNull
    private BigDecimal totalAmount;

    @Min(6)
    @NotNull
    private Integer term;

    @NotNull
    private BigDecimal monthlyPayment;
    @NotNull
    private BigDecimal rate;

    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}

