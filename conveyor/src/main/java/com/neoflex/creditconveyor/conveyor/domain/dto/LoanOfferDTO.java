package com.neoflex.creditconveyor.conveyor.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class LoanOfferDTO {
    public LoanOfferDTO(Long applicationId, BigDecimal requestedAmount, BigDecimal totalAmount, Integer term,
                        BigDecimal monthlyPayment, BigDecimal rate, Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        this.applicationId = applicationId;
        this.requestedAmount = requestedAmount;
        this.totalAmount = totalAmount;
        this.term = term;
        this.monthlyPayment = monthlyPayment;
        this.rate = rate;
        this.isInsuranceEnabled = isInsuranceEnabled;
        this.isSalaryClient = isSalaryClient;
    }

    private Long applicationId;
    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
