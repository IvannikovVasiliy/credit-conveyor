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
        Integer term = loanApplication.getTerm();
        BigDecimal insurancePrice = loanApplication.getAmount()
                .divide(BigDecimal.valueOf(Constants.INSURANCE_CONSTANT_DENOMINATOR))
                .multiply(BigDecimal.valueOf(loanApplication.getTerm()))
                .add(BigDecimal.valueOf(Constants.INSURANCE_CONSTANT_ARGUMENT));

        BigDecimal totalAmount1 = amountRequest;
        BigDecimal monthlyPayment1 = totalAmount1.divide(
                BigDecimal.valueOf(term), Constants.COUNT_DIGITS_AFTER_COMMA, RoundingMode.DOWN
        );

        BigDecimal totalAmount2 = amountRequest;
        BigDecimal monthlyPayment2 = totalAmount2.divide(
                BigDecimal.valueOf(term), Constants.COUNT_DIGITS_AFTER_COMMA, RoundingMode.DOWN
        );
        BigDecimal sale2 = BigDecimal.valueOf(-Constants.RATE_SALE_FOR_SALARY_CLIENTS);
        BigDecimal rate2 = Constants.BASE_RATE.add(sale2);

        BigDecimal totalAmount3 = loanApplication
                .getAmount()
                .add(insurancePrice);
        BigDecimal monthlyPayment3 = totalAmount3.divide(
                BigDecimal.valueOf(term), Constants.COUNT_DIGITS_AFTER_COMMA, RoundingMode.DOWN
        );
        BigDecimal sale3 = BigDecimal.valueOf(-Constants.RATE_SALE_FOR_INSURANCE);
        BigDecimal rate3 = Constants.BASE_RATE.add(sale3);

        BigDecimal totalAmount4 = loanApplication
                .getAmount()
                .add(insurancePrice);
        BigDecimal monthlyPayment4 = totalAmount4.divide(
                BigDecimal.valueOf(term), Constants.COUNT_DIGITS_AFTER_COMMA, RoundingMode.DOWN
        );
        BigDecimal sale4 = BigDecimal.valueOf(-Constants.RATE_SALE_FOR_INSURANCE_AND_AGREEMENT_SALARY_TRANSACTION);
        BigDecimal rate4 = Constants.BASE_RATE.add(sale4);

        List<LoanOfferDTO> loanOffers = new ArrayList<>(List.of(
                LoanOfferDTO
                        .builder()
                        .applicationId(Constants.APPLICATION_ID)
                        .requestedAmount(amountRequest)
                        .totalAmount(totalAmount1)
                        .term(loanApplication.getTerm())
                        .monthlyPayment(monthlyPayment1)
                        .rate(Constants.BASE_RATE)
                        .isInsuranceEnabled(false)
                        .isSalaryClient(false)
                        .build(),
                LoanOfferDTO
                        .builder()
                        .applicationId(Constants.APPLICATION_ID)
                        .requestedAmount(amountRequest)
                        .totalAmount(totalAmount2)
                        .term(loanApplication.getTerm())
                        .monthlyPayment(monthlyPayment2)
                        .rate(rate2)
                        .isInsuranceEnabled(false)
                        .isSalaryClient(true)
                        .build(),
                LoanOfferDTO
                        .builder()
                        .applicationId(Constants.APPLICATION_ID)
                        .requestedAmount(amountRequest)
                        .totalAmount(totalAmount3)
                        .term(loanApplication.getTerm())
                        .monthlyPayment(monthlyPayment3)
                        .rate(rate3)
                        .isInsuranceEnabled(true)
                        .isSalaryClient(false)
                        .build(),
                LoanOfferDTO
                        .builder()
                        .applicationId(Constants.APPLICATION_ID)
                        .requestedAmount(amountRequest)
                        .totalAmount(totalAmount4)
                        .term(loanApplication.getTerm())
                        .monthlyPayment(monthlyPayment4)
                        .rate(rate4)
                        .isInsuranceEnabled(true)
                        .isSalaryClient(true)
                        .build()
        ))
                .stream()
                .sorted((offer1, offer2) ->
                        offer1.getRate().compareTo(offer2.getRate()))
                .toList();

        log.debug("Response calculate offers. loanOffers={}", loanOffers);

        return loanOffers;
    }


}
