package com.neoflex.creditconveyor.conveyor.service.impl;

import com.neoflex.creditconveyor.conveyor.domain.constants.Constants;
import com.neoflex.creditconveyor.conveyor.domain.dto.*;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.Gender;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.MartialStatus;
import com.neoflex.creditconveyor.conveyor.domain.enumeration.Position;
import com.neoflex.creditconveyor.conveyor.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyor.conveyor.error.validation.Violation;
import com.neoflex.creditconveyor.conveyor.service.ConveyorService;
import com.neoflex.creditconveyor.conveyor.utils.DatesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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

        BigDecimal monthlyPayment1 = calcMonthlyPayment(term, Constants.BASE_RATE, amountRequest);
        BigDecimal totalAmount1 = calcTotalAmount(monthlyPayment1, term);

        BigDecimal sale2 = BigDecimal.valueOf(-Constants.RATE_SALE_FOR_SALARY_CLIENTS);
        BigDecimal rate2 = Constants.BASE_RATE.add(sale2);
        BigDecimal monthlyPayment2 = calcMonthlyPayment(term, rate2, amountRequest);
        BigDecimal totalAmount2 = calcTotalAmount(monthlyPayment2, term);

        BigDecimal sale3 = BigDecimal.valueOf(-Constants.RATE_SALE_FOR_INSURANCE);
        BigDecimal rate3 = Constants.BASE_RATE.add(sale3);
        BigDecimal monthlyPayment3 = calcMonthlyPayment(term, rate3, amountRequest);
        BigDecimal totalAmount3 = calcTotalAmount(monthlyPayment3, term);

        BigDecimal sale4 = BigDecimal.valueOf(-Constants.RATE_SALE_FOR_INSURANCE_AND_AGREEMENT_SALARY_TRANSACTION);
        BigDecimal rate4 = Constants.BASE_RATE.add(sale4);
        BigDecimal monthlyPayment4 = calcMonthlyPayment(term, rate4, amountRequest);
        BigDecimal totalAmount4 = calcTotalAmount(monthlyPayment4, term);

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

    @Override
    public CreditDTO validAndScoreAndCalcOffer(ScoringDataDTO scoringData) {
        log.debug("Input validAndScoreAndCalcOffer. scoringData={amount:{}, term:{}, firstName:{}, lastName:{}, middleName:{}, gender:{}, birthdate:{}, martialStatus:{}, dependentAmount:{}, employment:{}, account:{},  passportSeries:{}, passportNumber:{}, passportIssueDate:{}, passportIssueBranch:{}, isInsuranceEnabled:{}, isSalaryClient:{}}",
                scoringData.getAmount(), scoringData.getTerm(), scoringData.getFirstName(), scoringData.getLastName(), scoringData.getMiddleName(), scoringData.getGender(), scoringData.getBirthdate(), scoringData.getMartialStatus(), scoringData.getDependentAmount(), scoringData.getEmployment(), scoringData.getAccount(), scoringData.getPassportSeries(), scoringData.getPassportNumber(), scoringData.getPassportIssueDate(), scoringData.getPassportIssueBranch(), scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient());

        List<Violation> violations = refuseCredit(scoringData);
        if (violations.size() > 0) {
            log.debug("Error validAndScoreAndCalcOffer. violations:{}", violations);
            throw new ValidationAndScoringAndCalculationOfferException(violations);
        }

        CreditDTO creditDTO = CreditDTO.builder().build();
        calcRate(scoringData, creditDTO);
        BigDecimal psk = calcTotalAmount(scoringData.getAmount(), creditDTO.getTerm());
        BigDecimal monthPayment = calcMonthlyPayment(scoringData.getTerm(), creditDTO.getRate(), scoringData.getAmount());
        var paymentSchedules = doPaymentSchedule(scoringData, monthPayment, creditDTO.getRate());

        creditDTO.setAmount(scoringData.getAmount());
        creditDTO.setTerm(scoringData.getTerm());
        creditDTO.setMonthlyPayment(monthPayment);
        creditDTO.setPsk(psk);
        creditDTO.setIsInsuranceEnabled(scoringData.getIsInsuranceEnabled());
        creditDTO.setPaymentSchedule(paymentSchedules);

        log.debug("Output validAndScoreAndCalcOffer. creditDTO: {}", creditDTO);

        return creditDTO;
    }

    private List<Violation> refuseCredit(ScoringDataDTO scoringData) {
        log.debug("Input refuseCredit. scoringData={amount:{}, term:{}, firstName:{}, lastName:{}, middleName:{}, gender:{}, birthdate:{}, martialStatus:{}, dependentAmount:{}, employment:{}, account:{},  passportSeries:{}, passportNumber:{}, passportIssueDate:{}, passportIssueBranch:{}, isInsuranceEnabled:{}, isSalaryClient:{}}",
                scoringData.getAmount(), scoringData.getTerm(), scoringData.getFirstName(), scoringData.getLastName(), scoringData.getMiddleName(), scoringData.getGender(), scoringData.getBirthdate(), scoringData.getMartialStatus(), scoringData.getDependentAmount(), scoringData.getEmployment(), scoringData.getAccount(), scoringData.getPassportSeries(), scoringData.getPassportNumber(), scoringData.getPassportIssueDate(), scoringData.getPassportIssueBranch(), scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient());

        List<Violation> violations = new ArrayList<>();

        boolean isMartialStatusUnemployed = MartialStatus.UNEMPLOYED.equals(scoringData.getMartialStatus());
        if (isMartialStatusUnemployed) {
            violations.add(new Violation(
                    scoringData.getMartialStatus().getClass().getName(),
                    "Invalid value. Status shouldn't be UNEMPLOYED"
            ));
        }

        boolean isAmountTooMuch = scoringData
                .getAmount()
                .compareTo(BigDecimal.valueOf((long) scoringData.getDependentAmount()*Constants.COUNT_SALARIES)) > 0;
        if (isAmountTooMuch) {
            violations.add(new Violation(
                    scoringData.getAmount().getClass().getName(),
                    String.format("Invalid value. Amount should be less than %d salaries", Constants.COUNT_SALARIES)
            ));
        }

        Integer age = DatesUtil.getYears(scoringData.getBirthdate());
        boolean isAgeInvalid = age < Constants.MIN_AGE || age > Constants.MAX_AGE;
        if (isAgeInvalid) {
            violations.add(new Violation(
                    scoringData.getBirthdate().getClass().getName(),
                    String.format("Invalid value. Age should be more or equals than %d and less or equals then %d",
                            Constants.MIN_AGE, Constants.MAX_AGE)
            ));
        }

        boolean isExperienceSmall =
                scoringData.getEmployment().getWorkExperienceTotal() < Constants.MIN_VALID_TOTAL_EXPERIENCE ||
                scoringData.getEmployment().getWorkExperienceCurrent() < Constants.MIN_VALID_CURRENT_EXPERIENCE;
        if (isExperienceSmall) {
            violations.add(new Violation(
                    scoringData.getEmployment().getClass().getName(),
                    String.format("Invalid value. Total experience should be more or equals than %d. Current experience should be more or equals then %d",
                            Constants.MIN_VALID_TOTAL_EXPERIENCE, Constants.MIN_VALID_CURRENT_EXPERIENCE)
            ));
        }

        log.debug("Output refuseCredit. returned: {}", violations);
        return violations;
    }

    private void calcRate(ScoringDataDTO scoringData, CreditDTO creditDTO) {
        log.debug("Input calculateRate. scoringData: {amount:{}, term:{}, firstName:{}, lastName:{}, middleName:{}, gender:{}, birthdate:{}, martialStatus:{}, dependentAmount:{}, employment:{}, account:{},  passportSeries:{}, passportNumber:{}, passportIssueDate:{}, passportIssueBranch:{}, isInsuranceEnabled:{}, isSalaryClient:{}}",
                scoringData.getAmount(), scoringData.getTerm(), scoringData.getFirstName(), scoringData.getLastName(), scoringData.getMiddleName(), scoringData.getGender(), scoringData.getBirthdate(), scoringData.getMartialStatus(), scoringData.getDependentAmount(), scoringData.getEmployment(), scoringData.getAccount(), scoringData.getPassportSeries(), scoringData.getPassportNumber(), scoringData.getPassportIssueDate(), scoringData.getPassportIssueBranch(), scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient());

        BigDecimal rate = Constants.BASE_RATE;

        if (MartialStatus.SELF_EMPLOYED.equals(scoringData.getMartialStatus())) {
            BigDecimal rateSelfEmployed = creditDTO.getRate().add(BigDecimal.valueOf(Constants.RATE_FOR_SELF_EMPLOYED));
            creditDTO.setRate(rateSelfEmployed);
        }
        if (MartialStatus.OWNER_BUSINESS.equals(scoringData.getMartialStatus())) {
            BigDecimal rateOwnerBusiness = creditDTO.getRate().add(BigDecimal.valueOf(Constants.RATE_FOR_OWNER_BUSINESS));
            creditDTO.setRate(rateOwnerBusiness);
        }

        if (Position.AVERAGE_MANAGER.equals(scoringData.getEmployment().getPosition())) {
            BigDecimal rateAverageManager = creditDTO.getRate().add(BigDecimal.valueOf(Constants.RATE_FOR_AVERAGE_MANAGER));
            creditDTO.setRate(rateAverageManager);
        }
        if (Position.TOP_MANAGER.equals(scoringData.getEmployment().getPosition())) {
            BigDecimal rateTopManager = creditDTO.getRate().add(BigDecimal.valueOf(Constants.RATE_FOR_TOP_MANAGER));
            creditDTO.setRate(rateTopManager);
        }

        if (Gender.FEMALE.equals(scoringData.getGender())) {
            int age = DatesUtil.getYears(scoringData.getBirthdate());
            if (age >= Constants.MIN_AGE_SALE_FEMALE && age < Constants.MAX_AGE_SALE_FEMALE) {
                BigDecimal rateFemale = creditDTO.getRate().add(BigDecimal.valueOf(Constants.RATE_SALE_FOR_FEMALE));
                creditDTO.setRate(rateFemale);
            }
        }
        if (Gender.MALE.equals(scoringData.getGender())) {
            int age = DatesUtil.getYears(scoringData.getBirthdate());
            if (age >= Constants.MIN_AGE_SALE_MALE && age < Constants.MAX_AGE_SALE_MALE) {
                BigDecimal rateMale = creditDTO.getRate().add(BigDecimal.valueOf(Constants.RATE_SALE_FOR_MALE));
                creditDTO.setRate(rateMale);
            }
        }
        if (!Gender.MALE.equals(scoringData.getGender())) {
            if (!Gender.FEMALE.equals(scoringData.getGender())) {
                BigDecimal rateUnbinary = creditDTO.getRate().add(BigDecimal.valueOf(Constants.RATE_FOR_UNBINARY_GENDER));
                creditDTO.setRate(rateUnbinary);
            }
        }

        log.debug("Input calculateRate. scoringData: {amount:{}, term:{}, firstName:{}, lastName:{}, middleName:{}, gender:{}, birthdate:{}, martialStatus:{}, dependentAmount:{}, employment:{}, account:{},  passportSeries:{}, passportNumber:{}, passportIssueDate:{}, passportIssueBranch:{}, isInsuranceEnabled:{}, isSalaryClient:{}}, rate={}",
                scoringData.getAmount(), scoringData.getTerm(), scoringData.getFirstName(), scoringData.getLastName(), scoringData.getMiddleName(), scoringData.getGender(), scoringData.getBirthdate(), scoringData.getMartialStatus(), scoringData.getDependentAmount(), scoringData.getEmployment(), scoringData.getAccount(), scoringData.getPassportSeries(), scoringData.getPassportNumber(), scoringData.getPassportIssueDate(), scoringData.getPassportIssueBranch(), scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient(), creditDTO.getRate());
    }

    private BigDecimal calcTotalAmount(BigDecimal monthPayment, Integer term) {
        log.debug("Input calculate total Amount. monthPayment={}, term={}", monthPayment, term);
        return monthPayment.multiply(BigDecimal.valueOf(term));
    }

    private BigDecimal calcMonthlyPayment(Integer term, BigDecimal rate, BigDecimal amount) {
        log.debug("Input calculate monthly payment. term={}, rate={}, amount={}", term, rate, amount);

        BigDecimal monthRate = rate.divide(BigDecimal.valueOf(12)).divide(BigDecimal.valueOf(100));
        BigDecimal exp = monthRate.add(BigDecimal.ONE).pow(term);
        BigDecimal indexAnnuity = monthRate
                .multiply(exp)
                .divide(exp.add(BigDecimal.valueOf(-1)), Constants.ACCURACY, RoundingMode.DOWN);
        BigDecimal monthPayment = amount.multiply(indexAnnuity);

        log.debug("Result monthPayment={}", monthPayment);
        return monthPayment;
    }

    private List<PaymentScheduleElement> doPaymentSchedule(ScoringDataDTO scoringData, BigDecimal monthPayment, BigDecimal rate) {
        log.debug("Input calculateRate. scoringData: {amount:{}, term:{}, firstName:{}, lastName:{}, middleName:{}, gender:{}, birthdate:{}, martialStatus:{}, dependentAmount:{}, employment:{}, account:{},  passportSeries:{}, passportNumber:{}, passportIssueDate:{}, passportIssueBranch:{}, isInsuranceEnabled:{}, isSalaryClient:{}}; monthPayment={}, rate={}",
                scoringData.getAmount(), scoringData.getTerm(), scoringData.getFirstName(), scoringData.getLastName(), scoringData.getMiddleName(), scoringData.getGender(), scoringData.getBirthdate(), scoringData.getMartialStatus(), scoringData.getDependentAmount(), scoringData.getEmployment(), scoringData.getAccount(), scoringData.getPassportSeries(), scoringData.getPassportNumber(), scoringData.getPassportIssueDate(), scoringData.getPassportIssueBranch(), scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient(), monthPayment, rate);

        List<PaymentScheduleElement> paymentScheduleElements = new ArrayList<>();
        BigDecimal remainder = scoringData.getAmount();
        LocalDate date = LocalDate.now();
        for (int i = 0; i < scoringData.getTerm(); i++) {
            date = date.plusMonths(1);
            int countDaysOfYear = date.isLeapYear() ? Constants.COUNT_DAYS_IN_LEAP_YEAR : Constants.COUNT_DAYS_IN_NON_LEAP_YEAR;

            BigDecimal debtPayment = remainder
                    .multiply(rate
                            .add(BigDecimal.valueOf(Constants.MAX_PERCENT))
                            .divide(BigDecimal.valueOf(Constants.MAX_PERCENT)))
                    .multiply(BigDecimal.valueOf(date.getMonth().length(date.isLeapYear())))
                    .divide(BigDecimal.valueOf(countDaysOfYear));

            BigDecimal interestPayment = monthPayment.subtract(debtPayment);
            BigDecimal remainingDebt = remainder.subtract(interestPayment);

            PaymentScheduleElement currentPaymentSchedule =
                    new PaymentScheduleElement(i, date, monthPayment, interestPayment , debtPayment, remainingDebt);
            paymentScheduleElements.add(currentPaymentSchedule);
        }

        log.debug("result doPaymentSchedule. paymentScheduleElements: {}", paymentScheduleElements);

        return paymentScheduleElements;
    }
}
