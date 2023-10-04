package com.neoflex.creditconveyer.deal.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

