package com.neoflex.creditconveyer.application.controller;

import com.neoflex.creditconveyer.application.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.application.domain.dto.LoanApplicationResponseDTO;
import com.neoflex.creditconveyer.application.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.application.http.HttpConfig;
import com.neoflex.creditconveyer.application.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/{applicationId}")
    public ResponseEntity<LoanApplicationResponseDTO> getApplicationById(@PathVariable Long applicationId) {
        log.debug("Request for get application by id. applicationId={}", applicationId);

        LoanApplicationResponseDTO loanApplicationResponseDTO = applicationService.getApplicationById(applicationId);

        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpConfig.CORRELATION_ID_HEADER_CONFIG, List.of(applicationId.toString()));

        log.debug("Response receiving applications. received application with id={}", applicationId);
        return new ResponseEntity<>(loanApplicationResponseDTO, headers, HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }

    @PostMapping
    public ResponseEntity<List<LoanOfferDTO>> postApplication(@Valid @RequestBody LoanApplicationRequestDTO loanApplication) {
        log.debug("Request postApplication. loanApplication={amount: {}, term:{}, firstName:{}, lastName:{}, middleName:{}, email:{}, birthdate:{}, passportSeries:{}, passportNumber:{}}",
                loanApplication.getAmount(), loanApplication.getTerm(), loanApplication.getFirstName(), loanApplication.getLastName(), loanApplication.getMiddleName(), loanApplication.getEmail(), loanApplication.getBirthdate(), loanApplication.getPassportSeries(), loanApplication.getPassportNumber());

        List<LoanOfferDTO> loanOffers = applicationService.prescoringAndCalcPossibleConditions(loanApplication);

        log.debug("Response postApplication. loanOffers={}", loanOffers);
        return new ResponseEntity<>(loanOffers, HttpStatus.CREATED);
    }

    @PutMapping("/offer")
    public ResponseEntity<Void> putOffer(@RequestBody LoanOfferDTO loanOffer) {
        log.debug("Request putOffer. loanOffer={applicationId: {}, requestedAmount: {}, totalAmount: {}, term: {}, monthlyPayment: {}, rate: {}, isInsuranceEnabled: {}, isSalaryClient: {}}",
                loanOffer.getApplicationId(), loanOffer.getRequestedAmount(), loanOffer.getTotalAmount(), loanOffer.getTerm(), loanOffer.getMonthlyPayment(), loanOffer.getRate(), loanOffer.getIsInsuranceEnabled(), loanOffer.getIsSalaryClient());

        applicationService.chooseOffer(loanOffer);

        log.debug("Response putOffer");
        return ResponseEntity.accepted().build();
    }
}
