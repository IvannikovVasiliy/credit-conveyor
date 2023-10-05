package com.neoflex.creditconveyer.application.service.impl;

import com.neoflex.creditconveyer.application.domain.constant.Constants;
import com.neoflex.creditconveyer.application.domain.constant.RegExpConstants;
import com.neoflex.creditconveyer.application.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.application.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.application.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyer.application.error.validation.Violation;
import com.neoflex.creditconveyer.application.feign.DealFeignService;
import com.neoflex.creditconveyer.application.service.ApplicationService;
import com.neoflex.creditconveyer.application.validation.validator.AdultValidator;
import com.neoflex.creditconveyer.application.validation.validator.FirstNameValidator;
import com.neoflex.creditconveyer.application.validation.validator.LastNameValidator;
import com.neoflex.creditconveyer.application.validation.validator.MiddleNameValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final DealFeignService dealFeignService;

    @Override
    public List<LoanOfferDTO> prescoringAndCalcPossibleConditions(LoanApplicationRequestDTO loanApplication) {
        log.debug("Input prescoringAndCalcPossibleConditions. loanApplication={amount: {}, term:{}, firstName:{}, lastName:{}, middleName:{}, email:{}, birthdate:{}, passportSeries:{}, passportNumber:{}}",
                loanApplication.getAmount(), loanApplication.getTerm(), loanApplication.getFirstName(), loanApplication.getLastName(), loanApplication.getMiddleName(), loanApplication.getEmail(), loanApplication.getBirthdate(), loanApplication.getPassportSeries(), loanApplication.getPassportNumber());

        prescoring(loanApplication);
        List<LoanOfferDTO> loanOffers = dealFeignService.postDealApplication(loanApplication);
        loanOffers.sort((loanOffer1, loanOffer2) -> loanOffer2.getRate().compareTo(loanOffer1.getRate()));

        log.debug("Output prescoringAndCalcPossibleConditions. loanOffers={}", loanOffers);
        return loanOffers;
    }

    @Override
    public void chooseOffer(LoanOfferDTO loanOffer) {
        log.debug("Input chooseOffer. loanOffer={applicationId: {}, requestedAmount: {}, totalAmount: {}, term: {}, monthlyPayment: {}, rate: {}, isInsuranceEnabled: {}, isSalaryClient: {}}",
                loanOffer.getApplicationId(), loanOffer.getRequestedAmount(), loanOffer.getTotalAmount(), loanOffer.getTerm(), loanOffer.getMonthlyPayment(), loanOffer.getRate(), loanOffer.getIsInsuranceEnabled(), loanOffer.getIsSalaryClient());

        enrichLoanOffer(loanOffer);
        dealFeignService.putOffer(loanOffer);

        log.debug("Output chooseOffer");
    }

    private void prescoring(LoanApplicationRequestDTO loanApplication) {
        log.debug("Input prescoring. loanApplication={amount: {}, term:{}, firstName:{}, lastName:{}, middleName:{}, email:{}, birthdate:{}, passportSeries:{}, passportNumber:{}}",
                loanApplication.getAmount(), loanApplication.getTerm(), loanApplication.getFirstName(), loanApplication.getLastName(), loanApplication.getMiddleName(), loanApplication.getEmail(), loanApplication.getBirthdate(), loanApplication.getPassportSeries(), loanApplication.getPassportNumber());

        boolean isFirstNameValid = new FirstNameValidator().isValid(loanApplication.getFirstName());
        boolean isLastNameValid =  new LastNameValidator().isValid(loanApplication.getLastName());
        boolean isMiddleNameValid = new MiddleNameValidator().isValid(loanApplication.getMiddleName());
        boolean isAmountValid = loanApplication
                .getAmount()
                .compareTo(Constants.MIN_AMOUNT_CREDIT) >= 0;
        boolean isTermValid = loanApplication.getTerm() >= Constants.MIN_TERM;
        boolean isBirthdateValid = new AdultValidator().isValid(loanApplication.getBirthdate());
        boolean isEmailValid = true;
        if (loanApplication.getEmail() != null) {
             isEmailValid = Pattern
                    .compile(RegExpConstants.emailPattern)
                    .matcher(loanApplication.getEmail())
                    .matches();
        }
        boolean isSeriesPassportValid =
                loanApplication.getPassportSeries().length() == Constants.LENGTH_PASSPORT_SERIES;
        boolean isNumberPassportValid =
                loanApplication.getPassportNumber().length() == Constants.LENGTH_PASSPORT_NUMBER;

        List<Violation> violations = createViolations(
                isFirstNameValid, isLastNameValid, isMiddleNameValid, isAmountValid, isTermValid,
                isBirthdateValid, isEmailValid, isSeriesPassportValid, isNumberPassportValid
        );
        if (violations.size() > 0) {
            log.debug("Error prescoring. violations:{}", violations);
            throw new ValidationAndScoringAndCalculationOfferException(violations);
        }

        log.debug("Prescoring is valid. loanApplication={amount: {}, term:{}, firstName:{}, lastName:{}, middleName:{}, email:{}, birthdate:{}, passportSeries:{}, passportNumber:{}}",
                loanApplication.getAmount(), loanApplication.getTerm(), loanApplication.getFirstName(), loanApplication.getLastName(), loanApplication.getMiddleName(), loanApplication.getEmail(), loanApplication.getBirthdate(), loanApplication.getPassportSeries(), loanApplication.getPassportNumber());
    }

    private List<Violation> createViolations(boolean isFirstNameValid,
                                  boolean isLastNameValid,
                                  boolean isMiddleNameValid,
                                  boolean isAmountValid,
                                  boolean isTermValid,
                                  boolean isBirthdateValid,
                                  boolean isEmailValid,
                                  boolean isSeriesPassportValid,
                                  boolean isNumberPassportValid) {
        List<Violation> violations = new LinkedList<>();
        if (!isFirstNameValid) {
            Violation violation = new Violation("firstName", "Invalid value. Length should be between 2 and 30. Letters should be latin.");
            violations.add(violation);
        }
        if (!isLastNameValid) {
            Violation violation = new Violation("lastName", "Invalid value. Length should be between 2 and 30. Letters should be latin.");
            violations.add(violation);
        }
        if (!isMiddleNameValid) {
            Violation violation = new Violation("middleName", "Invalid value. Length should be between 2 and 30. Letters should be latin.");
            violations.add(violation);
        }
        if (!isAmountValid) {
            Violation violation = new Violation("amount", "Invalid value. Minimal amount value is 10000");
            violations.add(violation);
        }
        if (!isTermValid) {
            String errorMessage = String.format("Invalid value. Minimal term value is %d.", Constants.MIN_TERM);
            Violation violation = new Violation("term", errorMessage);
            violations.add(violation);
        }
        if (!isBirthdateValid) {
            String errorMessage = String.format("Invalid value. Birthdate is less than %s years", Constants.AGE_ADULT);
            Violation violation = new Violation("birthdate", errorMessage);
            violations.add(violation);
        }
        if (!isEmailValid) {
            Violation violation = new Violation("email", "Invalid value. Email is invalid");
            violations.add(violation);
        }
        if (!isSeriesPassportValid) {
            String errorMessage = String.format("Invalid value. Length passport series should be %d", Constants.LENGTH_PASSPORT_SERIES);
            Violation violation = new Violation("passportSeries", errorMessage);
            violations.add(violation);
        }
        if (!isNumberPassportValid) {
            String errorMessage = String.format("Invalid value. Length passport number should be %d", Constants.LENGTH_PASSPORT_NUMBER);
            Violation violation = new Violation("passportNumber", errorMessage);
            violations.add(violation);
        }

        return violations;
    }

    private void enrichLoanOffer(LoanOfferDTO loanOffer) {
        log.debug("Input enrichLoanOffer. loanOffer={applicationId: {}, requestedAmount: {}, totalAmount: {}, term: {}, monthlyPayment: {}, rate: {}, isInsuranceEnabled: {}, isSalaryClient: {}}",
                loanOffer.getApplicationId(), loanOffer.getRequestedAmount(), loanOffer.getTotalAmount(), loanOffer.getTerm(), loanOffer.getMonthlyPayment(), loanOffer.getRate(), loanOffer.getIsInsuranceEnabled(), loanOffer.getIsSalaryClient());

        if (null == loanOffer.getIsSalaryClient()) {
            loanOffer.setIsSalaryClient(false);
        }
        if (null == loanOffer.getIsInsuranceEnabled()) {
            loanOffer.setIsInsuranceEnabled(false);
        }

        log.debug("Output enrichLoanOffer. loanOffer={applicationId: {}, requestedAmount: {}, totalAmount: {}, term: {}, monthlyPayment: {}, rate: {}, isInsuranceEnabled: {}, isSalaryClient: {}}",
                loanOffer.getApplicationId(), loanOffer.getRequestedAmount(), loanOffer.getTotalAmount(), loanOffer.getTerm(), loanOffer.getMonthlyPayment(), loanOffer.getRate(), loanOffer.getIsInsuranceEnabled(), loanOffer.getIsSalaryClient());
    }
}
