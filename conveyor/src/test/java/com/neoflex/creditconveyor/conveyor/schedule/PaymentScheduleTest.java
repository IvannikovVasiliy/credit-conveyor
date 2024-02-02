package com.neoflex.creditconveyor.conveyor.schedule;

import com.neoflex.creditconveyor.conveyor.util.Constants;
import com.neoflex.creditconveyor.conveyor.domain.dto.EmploymentDTO;
import com.neoflex.creditconveyor.conveyor.domain.dto.PaymentScheduleElement;
import com.neoflex.creditconveyor.conveyor.domain.dto.ScoringDataDTO;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.EmploymentStatus;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.MartialStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class PaymentScheduleTest {

    @Test
    void createPaymentScheduleTest() {
        ScoringDataDTO scoringOwnerBusinessRequest = ScoringDataDTO
                .builder()
                .amount(BigDecimal.valueOf(10000))
                .term(7)
                .firstName("Ivan")
                .lastName("Ivanov")
                .birthdate(LocalDate.of(2000, 01, 01))
                .passportSeries("1234")
                .passportNumber("123456")
                .martialStatus(MartialStatus.SINGLE)
                .employment(EmploymentDTO
                        .builder()
                        .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                        .salary(BigDecimal.valueOf(1000))
                        .workExperienceTotal(13)
                        .workExperienceCurrent(4)
                        .build())
                .build();

        LocalDate date = LocalDate.of(2023, 10, 3);
        List<PaymentScheduleElement> paymentScheduleElementsExpected = List.of(
                new PaymentScheduleElement(1, date.plusMonths(1), BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1369.870917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(135.9).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(8630.129083).setScale(Constants.ACCURACY, RoundingMode.DOWN)),
                new PaymentScheduleElement(2, date.plusMonths(2), BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1392.2788085112).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(113.4921084887), BigDecimal.valueOf(7237.8502744888).setScale(Constants.ACCURACY, RoundingMode.DOWN)),
                new PaymentScheduleElement(3, date.plusMonths(3), BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1407.415472174).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(98.3554448259).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(5830.4348023148)),
                new PaymentScheduleElement(4, date.plusMonths(4), BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1426.7573743238).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(79.0135426761), BigDecimal.valueOf(4403.6774279910).setScale(Constants.ACCURACY, RoundingMode.DOWN)),
                new PaymentScheduleElement(5, date.plusMonths(5), BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1449.9428752899).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(55.82804171).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(2953.7345527011).setScale(Constants.ACCURACY, RoundingMode.DOWN)),
                new PaymentScheduleElement(6, date.plusMonths(6), BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1465.7421645918).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(40.0287524081).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1487.9923881093)),
                new PaymentScheduleElement(7, date.plusMonths(7), BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1486.2562627297).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(19.5146542702), BigDecimal.valueOf(1.7361253796)),
                new PaymentScheduleElement(8, date.plusMonths(7), BigDecimal.valueOf(1.7361253796).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(1.7361253796).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.ZERO, BigDecimal.ZERO)
        );

        List<PaymentScheduleElement> paymentScheduleElementsResponse =
                PaymentSchedule.createPaymentSchedule(scoringOwnerBusinessRequest, BigDecimal.valueOf(1505.770917).setScale(Constants.ACCURACY, RoundingMode.DOWN), BigDecimal.valueOf(16), date);

        assertEquals(paymentScheduleElementsExpected, paymentScheduleElementsResponse);
    }
}