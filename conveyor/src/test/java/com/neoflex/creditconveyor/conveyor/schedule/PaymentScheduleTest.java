package com.neoflex.creditconveyor.conveyor.schedule;

import com.neoflex.creditconveyor.conveyor.domain.constants.Constants;
import com.neoflex.creditconveyor.conveyor.domain.dto.EmploymentDTO;
import com.neoflex.creditconveyor.conveyor.domain.dto.PaymentScheduleElement;
import com.neoflex.creditconveyor.conveyor.domain.dto.ScoringDataDTO;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.MartialStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentScheduleTest {

    @Test
    public void createPaymentScheduleTest() {
        ScoringDataDTO scoringOwnerBusinessRequest = ScoringDataDTO
                .builder()
                .amount(BigDecimal.valueOf(10000))
                .term(7)
                .firstName("Ivan")
                .lastName("Ivanov")
                .birthdate(LocalDate.of(2000, 01, 01))
                .passportSeries("1234")
                .passportNumber("123456")
                .martialStatus(MartialStatus.OWNER_BUSINESS)
                .dependentAmount(1000)
                .employment(new EmploymentDTO(13, 4))
                .build();

        List<PaymentScheduleElement> paymentScheduleElementsExpected = List.of(
                new PaymentScheduleElement(1, LocalDate.now().plusMonths(1), BigDecimal.valueOf(1505.770917), BigDecimal.valueOf(1374.260917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(131.51).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(8625.739083).setScale(Constants.ACCURACY, RoundingMode.DOWN)),
                new PaymentScheduleElement(2, LocalDate.now().plusMonths(2), BigDecimal.valueOf(1505.770917), BigDecimal.valueOf(1388.555394118684).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(117.2155228813), BigDecimal.valueOf(7237.1836888814).setScale(Constants.ACCURACY, RoundingMode.DOWN)),
                new PaymentScheduleElement(3, LocalDate.now().plusMonths(3), BigDecimal.valueOf(1505.770917), BigDecimal.valueOf(1410.59699451608132).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(95.17392248391868).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(5826.5866943654)),
                new PaymentScheduleElement(4, LocalDate.now().plusMonths(4), BigDecimal.valueOf(1505.770917), BigDecimal.valueOf(1426.5931909615025643).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(79.1777260385), BigDecimal.valueOf(4399.9935034039).setScale(Constants.ACCURACY, RoundingMode.DOWN)),
                new PaymentScheduleElement(5, LocalDate.now().plusMonths(5), BigDecimal.valueOf(1505.770917), BigDecimal.valueOf(1446.142589740758165863).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(59.6283272592), BigDecimal.valueOf(2953.8509136632).setScale(Constants.ACCURACY, RoundingMode.DOWN)),
                new PaymentScheduleElement(6, LocalDate.now().plusMonths(6), BigDecimal.valueOf(1505.770917), BigDecimal.valueOf(1468.32318956995574008949).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(37.4477274300).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1485.5277240933)),
                new PaymentScheduleElement(7, LocalDate.now().plusMonths(7), BigDecimal.valueOf(1505.770917), BigDecimal.valueOf(1485.6391751652).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(20.1317418347), BigDecimal.valueOf(-0.1114510719))
        );

        List<PaymentScheduleElement> paymentScheduleElementsResponse = PaymentSchedule
                .createPaymentSchedule(scoringOwnerBusinessRequest, BigDecimal.valueOf(1505.7709170000), BigDecimal.valueOf(16));

        assertEquals(paymentScheduleElementsExpected, paymentScheduleElementsResponse);
    }
}