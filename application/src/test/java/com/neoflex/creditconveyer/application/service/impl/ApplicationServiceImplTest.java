package com.neoflex.creditconveyer.application.service.impl;

import com.neoflex.creditconveyer.application.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.application.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.application.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyer.application.feign.DealFeignService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ApplicationServiceImplTest {

    @Mock
    private DealFeignService dealFeignService;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    private BigDecimal requestedAmount = BigDecimal.valueOf(50000);
    private Long applicationId = 44L;

    @Test
    void prescoringThrowTest() {
        LoanApplicationRequestDTO invalidAmountLoanApplication = buildLoanApplicationRequestDTO(
                BigDecimal.valueOf(1), 7, LocalDate.of(2005, 1 ,1), "Ivan", "Ivanov", null, "qwerty@gmail.com", "1234", "123456"
        );
        LoanApplicationRequestDTO invalidTermLoanApplication = buildLoanApplicationRequestDTO(
                BigDecimal.valueOf(10000), 1, LocalDate.of(2005, 1 ,1), "Ivan", "Ivanov", null, "qwerty@gmail.com", "1234", "123456"
        );
        LoanApplicationRequestDTO invalidBirthdayLoanApplication = buildLoanApplicationRequestDTO(
                BigDecimal.valueOf(10000), 7, LocalDate.of(2021, 1 ,1), "Ivan", "Ivanov", null, "qwerty@gmail.com", "1234", "123456"
        );
        LoanApplicationRequestDTO invalidFirstLastMidNameLoanApplication = buildLoanApplicationRequestDTO(
                BigDecimal.valueOf(10000), 7, LocalDate.of(2001, 1 ,1), "Иван", "Иванов", null, "qwerty@gmail.com", "1234", "123456"
        );
        LoanApplicationRequestDTO invalidSeriesNumberLoanApplication = buildLoanApplicationRequestDTO(
                BigDecimal.valueOf(10000), 7, LocalDate.of(2001, 1 ,1), "Ivan", "Ivanov", null, "qwerty@gmail.com", "12345", "1234567"
        );
        LoanApplicationRequestDTO invalidEmailLoanApplication = buildLoanApplicationRequestDTO(
                BigDecimal.valueOf(10000), 7, LocalDate.of(2001, 1 ,1), "Иван", "Иванов", null, "qwerty@g", "12345", "1234567"
        );

        assertThrows(ValidationAndScoringAndCalculationOfferException.class, () ->
                applicationService.prescoringAndCalcPossibleConditions(invalidAmountLoanApplication));
        assertThrows(ValidationAndScoringAndCalculationOfferException.class, () ->
                applicationService.prescoringAndCalcPossibleConditions(invalidTermLoanApplication));
        assertThrows(ValidationAndScoringAndCalculationOfferException.class, () ->
                applicationService.prescoringAndCalcPossibleConditions(invalidFirstLastMidNameLoanApplication));
        assertThrows(ValidationAndScoringAndCalculationOfferException.class, () ->
                applicationService.prescoringAndCalcPossibleConditions(invalidBirthdayLoanApplication));
        assertThrows(ValidationAndScoringAndCalculationOfferException.class, () ->
                applicationService.prescoringAndCalcPossibleConditions(invalidSeriesNumberLoanApplication));
        assertThrows(ValidationAndScoringAndCalculationOfferException.class, () ->
                applicationService.prescoringAndCalcPossibleConditions(invalidEmailLoanApplication));
    }

    @Test
    void prescoringAndCalcPossibleConditionsTest() {
        LoanApplicationRequestDTO loanApplicationRequest = buildLoanApplicationRequestDTO(
                BigDecimal.valueOf(50000), 7, LocalDate.of(2005, 1 ,1), "Ivan", "Ivanov", null, "qwerty@gmail.com", "1234", "123456"
        );

        List<LoanOfferDTO> loanOffers = new ArrayList<>(List.of(
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(50836.7982850000), 7, BigDecimal.valueOf(7262.3997550000), BigDecimal.valueOf(5), true, false),
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(50668.8851550000), 7, BigDecimal.valueOf(7238.4121650000), BigDecimal.valueOf(4), true, true),
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(51511.2074750000), 7, BigDecimal.valueOf(7358.7439250000), BigDecimal.valueOf(9), false, true),
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(51680.4971900000), 7, BigDecimal.valueOf(7382.9281700000), BigDecimal.valueOf(10), false, false)
        ));
        List<LoanOfferDTO> loanOffersExpected = List.of(
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(51680.4971900000), 7, BigDecimal.valueOf(7382.9281700000), BigDecimal.valueOf(10), false, false),
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(51511.2074750000), 7, BigDecimal.valueOf(7358.7439250000), BigDecimal.valueOf(9), false, true),
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(50836.7982850000), 7, BigDecimal.valueOf(7262.3997550000), BigDecimal.valueOf(5), true, false),
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(50668.8851550000), 7, BigDecimal.valueOf(7238.4121650000), BigDecimal.valueOf(4), true, true)

        );
        when(dealFeignService.postDealApplication(loanApplicationRequest)).thenReturn(loanOffers);

        List<LoanOfferDTO> loanOffersResponse = applicationService.prescoringAndCalcPossibleConditions(loanApplicationRequest);
        assertEquals(loanOffersExpected, loanOffersResponse);
    }

    private LoanApplicationRequestDTO buildLoanApplicationRequestDTO(BigDecimal amount,
                                                                     Integer term,
                                                                     LocalDate birthdate,
                                                                     String firstName,
                                                                     String lastName,
                                                                     String middleName,
                                                                     String email,
                                                                     String passportSeries,
                                                                     String passportNumber) {
        LoanApplicationRequestDTO invalidBirthdayLoanApplication = new LoanApplicationRequestDTO();
        invalidBirthdayLoanApplication.setAmount(amount);
        invalidBirthdayLoanApplication.setTerm(term);
        invalidBirthdayLoanApplication.setBirthdate(birthdate);
        invalidBirthdayLoanApplication.setFirstName(firstName);
        invalidBirthdayLoanApplication.setLastName(lastName);
        invalidBirthdayLoanApplication.setMiddleName(middleName);
        invalidBirthdayLoanApplication.setEmail(email);
        invalidBirthdayLoanApplication.setPassportSeries(passportSeries);
        invalidBirthdayLoanApplication.setPassportNumber(passportNumber);
        return invalidBirthdayLoanApplication;
    }
}