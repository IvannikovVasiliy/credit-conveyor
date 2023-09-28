package com.neoflex.creditconveyor.conveyor.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.creditconveyor.conveyor.domain.constants.Constants;
import com.neoflex.creditconveyor.conveyor.domain.dto.*;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.MartialStatus;
import com.neoflex.creditconveyor.conveyor.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyor.conveyor.error.validation.Violation;
import lombok.RequiredArgsConstructor;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@RequiredArgsConstructor
class ConveyorServiceImplTest {

    @Autowired
    private ConveyorServiceImpl conveyorService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
        ScoringDataDTO scoringDataRequest = new ScoringDataDTO(BigDecimal.valueOf(10000), 7, "Ivan", "Ivanov",
                LocalDate.of(2000, 01, 01), "1234", "123456",
                MartialStatus.OWNER_BUSINESS, 1000, new EmploymentDTO(13, 4));
        ScoringDataDTO scoringDataUnemployedRequest = new ScoringDataDTO(BigDecimal.valueOf(10000), 7, "Ivan", "Ivanov",
                LocalDate.of(2000, 01, 01), "1234", "123456",
                MartialStatus.UNEMPLOYED, 1000, new EmploymentDTO(13, 4));

        CreditDTO expectedCredit = CreditDTO.builder()
                .amount(BigDecimal.valueOf(10000))
                .term(7)
                .monthlyPayment(BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN))
                .rate(BigDecimal.valueOf(16))
                .psk(BigDecimal.valueOf(10540.396419).setScale(Constants.ACCURACY, RoundingMode.DOWN))
                .build();

        CreditDTO responseCredit = conveyorService.validAndScoreAndCalcOffer(scoringDataRequest);

        List<PaymentScheduleElement> paymentScheduleElementsExpected = List.of(
                new PaymentScheduleElement(1, LocalDate.now().plusMonths(1), BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1374.260917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(131.51).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(8625.739083).setScale(Constants.ACCURACY, RoundingMode.DOWN)),
                new PaymentScheduleElement(2, LocalDate.now().plusMonths(2), BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1388.555394118684).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(117.2155228813), BigDecimal.valueOf(7237.1836888814).setScale(Constants.ACCURACY, RoundingMode.DOWN)),
                new PaymentScheduleElement(3, LocalDate.now().plusMonths(3), BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1410.59699451608132).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(95.17392248391868).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(5826.5866943654)),
                new PaymentScheduleElement(4, LocalDate.now().plusMonths(4), BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1426.5931909615025643).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(79.1777260385), BigDecimal.valueOf(4399.9935034039).setScale(Constants.ACCURACY, RoundingMode.DOWN)),
                new PaymentScheduleElement(5, LocalDate.now().plusMonths(5), BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1446.142589740758165863).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(59.6283272592), BigDecimal.valueOf(2953.8509136632).setScale(Constants.ACCURACY, RoundingMode.DOWN)),
                new PaymentScheduleElement(6, LocalDate.now().plusMonths(6), BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1468.32318956995574008949).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(37.4477274300).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1485.5277240933)),
                new PaymentScheduleElement(7, LocalDate.now().plusMonths(7), BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1485.6391751652).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(20.1317418347), BigDecimal.valueOf(-0.1114510719))
        );

        List<PaymentScheduleElement> paymentScheduleElementsResponse = responseCredit.getPaymentSchedule();
        responseCredit.setPaymentSchedule(null);

        assertEquals(expectedCredit, responseCredit);
        assertEquals(paymentScheduleElementsExpected, paymentScheduleElementsResponse);

        assertThrows(ValidationAndScoringAndCalculationOfferException.class, () ->
                conveyorService.validAndScoreAndCalcOffer(scoringDataUnemployedRequest));
    }

    @Test
    public void refuseCreditThrowExceptionTest() {
        ScoringDataDTO scoringDataUnemployed = new ScoringDataDTO(BigDecimal.valueOf(10000), 7, "Ivan", "Ivanov",
                LocalDate.of(2000, 01, 01), "1234", "123456",
                MartialStatus.UNEMPLOYED, 1000, new EmploymentDTO(13, 4));
        ScoringDataDTO scoringDataSmallSalary = new ScoringDataDTO(BigDecimal.valueOf(10000), 7, "Ivan", "Ivanov",
                LocalDate.of(2000, 01, 01), "1234", "123456",
                MartialStatus.SELF_EMPLOYED, 100, new EmploymentDTO(13, 4));
        ScoringDataDTO scoringDataExperienceSmall = new ScoringDataDTO(BigDecimal.valueOf(10000), 7, "Ivan", "Ivanov",
                LocalDate.of(2000, 01, 01), "1234", "123456",
                MartialStatus.SELF_EMPLOYED, 100, new EmploymentDTO(1, 1));

        assertThrows(ValidationAndScoringAndCalculationOfferException.class, () ->
                conveyorService.validAndScoreAndCalcOffer(scoringDataExperienceSmall));
        assertThrows(ValidationAndScoringAndCalculationOfferException.class, () ->
                conveyorService.validAndScoreAndCalcOffer(scoringDataSmallSalary));
        assertThrows(ValidationAndScoringAndCalculationOfferException.class, () ->
                conveyorService.validAndScoreAndCalcOffer(scoringDataExperienceSmall));

//        List<Violation> violationsExpected = new ArrayList<>();
//        Violation unemployedViolation = new Violation("martialStatus", "Invalid value. Status shouldn't be UNEMPLOYED");
//        violationsExpected.add(unemployedViolation);
//        Violation amountViolation = new Violation(
//                "amount",
//                String.format("Invalid value. Amount should be less than %d salaries", Constants.COUNT_SALARIES)
//        );
//        violationsExpected.add(amountViolation);
//        Violation birthdateViolation = new Violation(
//                "birthdate",
//                String.format("Invalid value. Age should be more or equals than %d and less or equals then %d",
//                        Constants.MIN_AGE, Constants.MAX_AGE)
//        );
//        violationsExpected.add(birthdateViolation);
//        Violation employmentViolation =  new Violation(
//                "employment",
//                String.format("Invalid value. Total experience should be more or equals than %d. Current experience should be more or equals then %d",
//                        Constants.MIN_VALID_TOTAL_EXPERIENCE, Constants.MIN_VALID_CURRENT_EXPERIENCE)
//        );
//
//        List<Violation> violationsResponse = conveyorService
//
//        assertTrue(violations.contains(unemployedViolation));
//        assertTrue(violations.contains(amountViolation));
//        assertTrue(violations.contains(birthdateViolation));
//        assertTrue(violations.contains(employmentViolation));
//        assertEquals(violations.size(), 4);
    }
}