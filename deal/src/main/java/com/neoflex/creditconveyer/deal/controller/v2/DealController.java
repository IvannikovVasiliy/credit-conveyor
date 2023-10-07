package com.neoflex.creditconveyer.deal.controller.v2;


import com.neoflex.creditconveyer.deal.domain.dto.FinishRegistrationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.service.DealService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v2/deal")
public class DealController {

    @Qualifier("dealServiceSenderEmailImpl") private final DealService dealService;

    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> postApplication(@Valid @RequestBody LoanApplicationRequestDTO loanApplication) {
        log.debug("Request postApplication. loanApplicationRequest={amount: {}, term:{}, firstName:{}, lastName:{}, middleName:{}, email:{}, birthdate:{}, passportSeries:{}, passportNumber:{}}",
                loanApplication.getAmount(), loanApplication.getTerm(), loanApplication.getFirstName(), loanApplication.getLastName(), loanApplication.getMiddleName(), loanApplication.getEmail(), loanApplication.getBirthdate(), loanApplication.getPassportSeries(), loanApplication.getPassportNumber());

        List<LoanOfferDTO> loanOffers = dealService.calculateCreditConditions(loanApplication);

        log.debug("Response postApplication. {}", loanOffers);

        return new ResponseEntity<>(loanOffers, HttpStatus.OK);
    }

    @PutMapping("/offer")
    public ResponseEntity<Void> chooseOffer(@Valid @RequestBody LoanOfferDTO loanOffer) {
        log.debug("Request chooseOffer. loanOffer={applicationId: {}, requestedAmount: {}, totalAmount: {}, term: {}, monthlyPayment: {}, rate: {}, isInsuranceEnabled: {}, isSalaryClient: {}}",
                loanOffer.getApplicationId(), loanOffer.getRequestedAmount(), loanOffer.getTotalAmount(), loanOffer.getTerm(), loanOffer.getMonthlyPayment(), loanOffer.getRate(), loanOffer.getIsInsuranceEnabled(), loanOffer.getIsSalaryClient());

        dealService.chooseOffer(loanOffer);

        log.info("Response chooseOffer");
        return ResponseEntity.ok().build();
    }

    @PutMapping("/calculate/{applicationId}")
    public ResponseEntity<Void> calculateByAppId(@NotNull @PathVariable Long applicationId,
                                                 @Valid @RequestBody FinishRegistrationRequestDTO finishRegistration) {
        log.debug("Request calculateById. applicationId={}, finishRegistration={ gender: {}, martialStatus: {}, dependentAmount: {}, passportIssueDate: {}, passportIssueBranch: {}, employment: { employmentStatus: [}, employerINN: {}, salary: {}, position: {}, workExperienceTotal: {}, workExperienceCurrent: {} } }",
                applicationId, finishRegistration.getGender(), finishRegistration.getMaritalStatus(), finishRegistration.getDependentAmount(), finishRegistration.getPassportIssueDate(), finishRegistration.getPassportIssueBranch(), finishRegistration.getEmployment().getEmploymentStatus(), finishRegistration.getEmployment().getEmployerINN(), finishRegistration.getEmployment().getPosition(), finishRegistration.getEmployment().getWorkExperienceTotal(), finishRegistration.getEmployment().getWorkExperienceCurrent());

        dealService.finishRegistrationAndCalcAmountCredit(applicationId, finishRegistration);

        log.info("Response calculateByAppId");

        return ResponseEntity.ok().build();
    }
}
