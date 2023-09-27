package com.neoflex.creditconveyor.conveyor.service.impl;

import com.neoflex.creditconveyor.conveyor.domain.Constants;
import com.neoflex.creditconveyor.conveyor.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyor.conveyor.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyor.conveyor.service.ConveyorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Validated
@Slf4j
public class ConveyorServiceImpl implements ConveyorService {

    @Override
    public List<LoanOfferDTO> createLoanOffer(LoanApplicationRequestDTO loanApplication) {
        log.debug("Request calculate offers. loanApplicationRequest={ amount:{}, term:{}, firstName:{}, lastName={}, middleName={}, email: {}, birthdate; {}, passportSeries;{}, passportNumber: {} }",
                loanApplication.getAmount(), loanApplication.getTerm(), loanApplication.getFirstName(), loanApplication.getLastName(), loanApplication.getMiddleName(), loanApplication.getEmail(), loanApplication.getBirthdate(), loanApplication.getPassportSeries(), loanApplication.getPassportNumber());

        BigDecimal amountRequest = loanApplication.getAmount();
        BigDecimal monthlyPayment = amountRequest.divide(
                BigDecimal.valueOf(loanApplication.getTerm()), Constants.COUNT_DIGITS_AFTER_COMMA, RoundingMode.DOWN
        );

        BigDecimal sale2 = BigDecimal.valueOf(-Constants.RATE_SALE_FOR_SALARY_CLIENTS);
        BigDecimal rate2 = Constants.BASE_RATE.add(sale2);

        BigDecimal insurancePrice = loanApplication.getAmount()
                .divide(BigDecimal.valueOf(Constants.INSURANCE_CONSTANT_DENOMINATOR))
                .multiply(BigDecimal.valueOf(loanApplication.getTerm()))
                .add(BigDecimal.valueOf(Constants.INSURANCE_CONSTANT_ARGUMENT));
        BigDecimal totalAmountWithInsurance = loanApplication
                        .getAmount()
                        .add(insurancePrice);
        BigDecimal sale3 = BigDecimal.valueOf(-Constants.RATE_SALE_FOR_INSURANCE);
        BigDecimal rate3 = Constants.BASE_RATE.add(sale3);

        BigDecimal sale4 = BigDecimal.valueOf(-Constants.RATE_SALE_FOR_INSURANCE_AND_AGREEMENT_SALARY_TRANSACTION);
        BigDecimal rate4 = Constants.BASE_RATE.add(sale4);

        List<LoanOfferDTO> loanOffers = new ArrayList<>(List.of(
                LoanOfferDTO
                        .builder()
                        .applicationId(Constants.APPLICATION_ID)
                        .requestedAmount(amountRequest)
                        .totalAmount(amountRequest)
                        .term(loanApplication.getTerm())
                        .monthlyPayment(monthlyPayment)
                        .rate(Constants.BASE_RATE)
                        .isInsuranceEnabled(false)
                        .isSalaryClient(false)
                        .build(),
                LoanOfferDTO
                        .builder()
                        .applicationId(Constants.APPLICATION_ID)
                        .requestedAmount(amountRequest)
                        .totalAmount(amountRequest)
                        .term(loanApplication.getTerm())
                        .monthlyPayment(monthlyPayment)
                        .rate(rate2)
                        .isInsuranceEnabled(false)
                        .isSalaryClient(true)
                        .build(),
                LoanOfferDTO
                        .builder()
                        .applicationId(Constants.APPLICATION_ID)
                        .requestedAmount(amountRequest)
                        .totalAmount(totalAmountWithInsurance)
                        .term(loanApplication.getTerm())
                        .monthlyPayment(monthlyPayment)
                        .rate(rate3)
                        .isInsuranceEnabled(true)
                        .isSalaryClient(false)
                        .build(),
                LoanOfferDTO
                        .builder()
                        .applicationId(Constants.APPLICATION_ID)
                        .requestedAmount(amountRequest)
                        .totalAmount(totalAmountWithInsurance)
                        .term(loanApplication.getTerm())
                        .monthlyPayment(monthlyPayment)
                        .rate(rate4)
                        .isInsuranceEnabled(true)
                        .isSalaryClient(true)
                        .build()
        ))
                .stream()
                .sorted((offer1, offer2) ->
                        offer1.getRate().compareTo(offer2.getRate()))
                .toList();

        return loanOffers;
    }
}
