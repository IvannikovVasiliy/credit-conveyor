package com.neoflex.creditconveyor.conveyor.service.impl;

import com.neoflex.creditconveyor.conveyor.domain.Constants;
import com.neoflex.creditconveyor.conveyor.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyor.conveyor.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyor.conveyor.service.ConveyorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Validated
@Slf4j
public class ConveyorServiceImpl implements ConveyorService {

    @Override
    public List<LoanOfferDTO> createLoanOffer(LoanApplicationRequestDTO loanApplication) {
        log.debug("Request calculate offers. loanApplicationRequest={ amount:{}, term:{}, firstName:{}, lastName={}, middleName={}, email: {}, birthdate; {}, passportSeries;{}, passportNumber: {} }",
                loanApplication.getAmount(), loanApplication.getTerm(), loanApplication.getFirstName(), loanApplication.getLastName(), loanApplication.getMiddleName(), loanApplication.getEmail(), loanApplication.getBirthdate(), loanApplication.getPassportSeries(), loanApplication.getPassportNumber());

        BigDecimal amountRequest = loanApplication.getAmount();
        BigDecimal monthlyPayment = amountRequest.divide(BigDecimal.valueOf(Constants.COUNT_MONTHS));

        BigDecimal rate2 = Constants.BASE_RATE.add(BigDecimal.valueOf(-1));
        BigDecimal totalAmount3 = loanApplication.getAmount().add(BigDecimal.valueOf(Constants.INSURANCE_PRICE));
        BigDecimal rate3 = Constants.BASE_RATE.add(BigDecimal.valueOf(-5));

        List<LoanOfferDTO> loanOffers = List.of(
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
                        .totalAmount(totalAmount3)
                        .term(loanApplication.getTerm())
                        .monthlyPayment(monthlyPayment)
                        .rate(rate3)
                        .isInsuranceEnabled(true)
                        .isSalaryClient(false)
                        .build(),
        );

        return loanOffers;
    }
}
