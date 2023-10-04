package com.neoflex.creditconveyor.conveyor.service.impl;

import com.neoflex.creditconveyor.conveyor.domain.constants.Constants;
import com.neoflex.creditconveyor.conveyor.domain.dto.*;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.EmploymentStatus;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.Gender;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.MartialStatus;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.EmploymentPosition;
import com.neoflex.creditconveyor.conveyor.error.exception.ValidationAndScoringAndCalculationOfferException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@RequiredArgsConstructor
class ConveyorServiceImplTest {

    @Autowired
    private ConveyorServiceImpl conveyorService;

    @BeforeAll
    public static void init() {
        employment = EmploymentDTO
                .builder()
                .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                .position(EmploymentPosition.WORKER)
                .salary(BigDecimal.valueOf(1000))
                .workExperienceTotal(13)
                .workExperienceCurrent(4)
                .build();
    }

    private static EmploymentDTO employment;

    @Test
    public void createLoanOfferTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO(
                        BigDecimal.valueOf(50000), 7, "Ivan", "Ivanov", LocalDate.of(2000, 01, 01), "1234", "123456"
                );

        LoanOfferDTO loanOfferResponse0 = new LoanOfferDTO(123456L, BigDecimal.valueOf(50000),
                new BigDecimal(50668.885155).setScale(Constants.ACCURACY, RoundingMode.DOWN), 7,
                BigDecimal.valueOf(7238.412165).setScale(Constants.ACCURACY, RoundingMode.DOWN),
                BigDecimal.valueOf(4), true, true);
        LoanOfferDTO loanOfferResponse1 = new LoanOfferDTO(123456L, BigDecimal.valueOf(50000),
                BigDecimal.valueOf(50836.798285).setScale(Constants.ACCURACY, RoundingMode.DOWN), 7,
                BigDecimal.valueOf(7262.399755).setScale(Constants.ACCURACY, RoundingMode.DOWN),
                BigDecimal.valueOf(5), true, false);
        LoanOfferDTO loanOfferResponse2 = new LoanOfferDTO(123456L, BigDecimal.valueOf(50000),
                BigDecimal.valueOf(51511.207475).setScale(Constants.ACCURACY, RoundingMode.DOWN), 7,
                BigDecimal.valueOf(7358.743925).setScale(Constants.ACCURACY, RoundingMode.DOWN),
                BigDecimal.valueOf(9), false, true);
        LoanOfferDTO loanOfferResponse3 = new LoanOfferDTO(123456L, BigDecimal.valueOf(50000),
                BigDecimal.valueOf(51680.49719).setScale(Constants.ACCURACY, RoundingMode.DOWN), 7,
                BigDecimal.valueOf(7382.92817).setScale(Constants.ACCURACY, RoundingMode.DOWN),
                BigDecimal.valueOf(10), false, false);

        List<LoanOfferDTO> loanOffers = conveyorService.createLoanOffer(loanApplicationRequestDTO);

        assertEquals(loanOfferResponse0, loanOffers.get(0));
        assertEquals(loanOfferResponse1, loanOffers.get(1));
        assertEquals(loanOfferResponse2, loanOffers.get(2));
        assertEquals(loanOfferResponse3, loanOffers.get(3));
    }

    @Test
    public void validAndScoreAndCalcOffer() throws URISyntaxException, IOException {
        ScoringDataDTO scoringOwnerBusinessRequest = ScoringDataDTO
                .builder()
                .amount(BigDecimal.valueOf(10000))
                .term(7)
                .firstName("Ivan")
                .lastName("Ivanov")
                .gender(Gender.NON_BINARY)
                .birthdate(LocalDate.of(2000, 01, 01))
                .passportSeries("1234")
                .passportNumber("123456")
                .martialStatus(MartialStatus.SINGLE)
                .employment(employment)
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        ScoringDataDTO scoringDataUnemployedRequest = ScoringDataDTO
                .builder()
                .amount(BigDecimal.valueOf(10000)).term(7)
                .firstName("Ivan")
                .lastName("Ivanov")
                .gender(Gender.MALE)
                .birthdate(LocalDate.of(2000, 01, 01))
                .passportSeries("1234")
                .passportNumber("123456")
                .martialStatus(MartialStatus.SINGLE)
                .employment(EmploymentDTO
                        .builder()
                        .employmentStatus(EmploymentStatus.UNEMPLOYED)
                        .workExperienceTotal(0)
                        .workExperienceCurrent(0)
                        .build())
                .isSalaryClient(false)
                .isInsuranceEnabled(false)
                .build();

        CreditDTO expectedCredit = CreditDTO
                .builder()
                .amount(BigDecimal.valueOf(10000))
                .term(7)
                .monthlyPayment(BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN))
                .rate(BigDecimal.valueOf(16))
                .psk(BigDecimal.valueOf(10540.396419).setScale(Constants.ACCURACY, RoundingMode.DOWN))
                .isSalaryClient(false)
                .isInsuranceEnabled(false)
                .build();

        CreditDTO responseCredit = conveyorService.validAndScoreAndCalcOffer(scoringOwnerBusinessRequest);
        responseCredit.setPaymentSchedule(null);

        assertEquals(expectedCredit, responseCredit);
        assertThrows(ValidationAndScoringAndCalculationOfferException.class, () ->
                conveyorService.validAndScoreAndCalcOffer(scoringDataUnemployedRequest));
    }

    @Test
    public void refuseCreditThrowExceptionTest() {
        ScoringDataDTO scoringDataSmallSalary = ScoringDataDTO
                .builder()
                .amount(BigDecimal.valueOf(10000)).term(7)
                .firstName("Ivan")
                .lastName("Ivanov")
                .gender(Gender.MALE)
                .birthdate(LocalDate.of(2000, 01, 01))
                .passportSeries("1234")
                .passportNumber("123456")
                .martialStatus(MartialStatus.SINGLE)
                .employment(EmploymentDTO
                        .builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .salary(BigDecimal.valueOf(100))
                        .workExperienceTotal(13)
                        .workExperienceCurrent(4)
                        .build())
                .isSalaryClient(false)
                .isInsuranceEnabled(false)
                .build();
        ScoringDataDTO scoringDataExperienceSmall = ScoringDataDTO
                .builder()
                .amount(BigDecimal.valueOf(10000)).term(7)
                .firstName("Ivan")
                .lastName("Ivanov")
                .gender(Gender.MALE)
                .birthdate(LocalDate.of(2000, 01, 01))
                .passportSeries("1234")
                .passportNumber("123456")
                .martialStatus(MartialStatus.SINGLE)
                .dependentAmount(1000)
                .employment(EmploymentDTO
                        .builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .salary(BigDecimal.valueOf(1000))
                        .workExperienceCurrent(1)
                        .workExperienceTotal(1)
                        .build())
                .isSalaryClient(false)
                .isInsuranceEnabled(false)
                .build();

        assertThrows(ValidationAndScoringAndCalculationOfferException.class, () ->
                conveyorService.validAndScoreAndCalcOffer(scoringDataExperienceSmall));
        assertThrows(ValidationAndScoringAndCalculationOfferException.class, () ->
                conveyorService.validAndScoreAndCalcOffer(scoringDataSmallSalary));
        assertThrows(ValidationAndScoringAndCalculationOfferException.class, () ->
                conveyorService.validAndScoreAndCalcOffer(scoringDataExperienceSmall));
    }

    @Test
    public void calcRateTest() {
        ScoringDataDTO scoringDataDTO = ScoringDataDTO
                .builder()
                .martialStatus(MartialStatus.MARRIED)
                .employment(EmploymentDTO
                        .builder()
                        .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                        .position(EmploymentPosition.TOP_MANAGER)
                        .salary(BigDecimal.valueOf(100_000))
                        .workExperienceTotal(36)
                        .workExperienceCurrent(12)
                        .build())
                .gender(Gender.FEMALE)
                .birthdate(LocalDate.of(1980, 01, 01))
                .passportSeries("1234")
                .passportNumber("123456")
                .dependentAmount(2)
                .firstName("Ivan")
                .lastName("Ivanov")
                .gender(Gender.MALE)
                .amount(BigDecimal.valueOf(10000))
                .term(6)
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        BigDecimal expectedRate = BigDecimal.valueOf(4);
        CreditDTO creditResponse = conveyorService.validAndScoreAndCalcOffer(scoringDataDTO);
        BigDecimal responseRate = creditResponse.getRate();

        assertEquals(expectedRate, responseRate);
    }
}