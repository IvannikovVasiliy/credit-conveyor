package com.neoflex.creditconveyor.conveyor.schedule;

import com.neoflex.creditconveyor.conveyor.domain.dto.PaymentScheduleElement;
import com.neoflex.creditconveyor.conveyor.domain.dto.ScoringDataDTO;
import com.neoflex.creditconveyor.conveyor.util.Constants;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PaymentSchedule {

    public static List<PaymentScheduleElement> createPaymentSchedule(ScoringDataDTO scoringData, BigDecimal monthPayment, BigDecimal rate, LocalDate date) {
        log.debug("Input calculateRate. scoringData: {amount:{}, term:{}, firstName:{}, lastName:{}, middleName:{}, gender:{}, birthdate:{}, martialStatus:{}, dependentAmount:{}, employment:{}, account:{},  passportSeries:{}, passportNumber:{}, passportIssueDate:{}, passportIssueBranch:{}, isInsuranceEnabled:{}, isSalaryClient:{}}; monthPayment={}, rate={}",
                scoringData.getAmount(), scoringData.getTerm(), scoringData.getFirstName(), scoringData.getLastName(), scoringData.getMiddleName(), scoringData.getGender(), scoringData.getBirthdate(), scoringData.getMartialStatus(), scoringData.getDependentAmount(), scoringData.getEmployment(), scoringData.getAccount(), scoringData.getPassportSeries(), scoringData.getPassportNumber(), scoringData.getPassportIssueDate(), scoringData.getPassportIssueBranch(), scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient(), monthPayment, rate);

        List<PaymentScheduleElement> paymentScheduleElements = new ArrayList<>();
        BigDecimal remainder = scoringData.getAmount();

        for (int i = 0; i < scoringData.getTerm(); i++) {
            int countDaysOfYear = date.isLeapYear() ? Constants.COUNT_DAYS_IN_LEAP_YEAR : Constants.COUNT_DAYS_IN_NON_LEAP_YEAR;

            BigDecimal debtPayment = remainder
                    .multiply(rate.divide(BigDecimal.valueOf(Constants.MAX_PERCENT)))
                    .multiply(BigDecimal.valueOf(date.getMonth().length(date.isLeapYear())))
                    .divide(BigDecimal.valueOf(countDaysOfYear), Constants.ACCURACY_DEBT_PAYMENT);

            BigDecimal interestPayment = monthPayment.subtract(debtPayment);
            interestPayment = interestPayment.setScale(Constants.ACCURACY, RoundingMode.DOWN);
            BigDecimal remainingDebt = remainder.subtract(interestPayment);
            remainder = remainder.subtract(interestPayment);

            debtPayment = debtPayment.setScale(Constants.ACCURACY, RoundingMode.DOWN);

            date = date.plusMonths(1);
            PaymentScheduleElement currentPaymentSchedule =
                    new PaymentScheduleElement(i+1, date, monthPayment, interestPayment , debtPayment, remainingDebt);
            paymentScheduleElements.add(currentPaymentSchedule);
        }

        PaymentScheduleElement lastPaymentSchedule = paymentScheduleElements.get(scoringData.getTerm()-1);
        boolean hasDebt = lastPaymentSchedule.getRemainingDebt().compareTo(BigDecimal.ZERO) > 0;
        if (hasDebt) {
            PaymentScheduleElement paymentScheduleElement = new PaymentScheduleElement(lastPaymentSchedule.getNumber()+1, lastPaymentSchedule.getDate(), lastPaymentSchedule.getRemainingDebt(), lastPaymentSchedule.getRemainingDebt(), BigDecimal.ZERO, BigDecimal.ZERO);
            paymentScheduleElements.add(paymentScheduleElement);
        }

        log.debug("result doPaymentSchedule. paymentScheduleElements: {}", paymentScheduleElements);

        return paymentScheduleElements;
    }
}
