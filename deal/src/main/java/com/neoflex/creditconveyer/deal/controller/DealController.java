package com.neoflex.creditconveyer.deal.controller;

import com.neoflex.creditconveyer.deal.domain.dto.FinishRegistrationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/deal")
public class DealController {

    private final DealService dealService;

    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> postApplication(@RequestBody LoanApplicationRequestDTO loanApplication) {
        log.debug("Request postApplication. loanApplicationRequest={amount: {}, term:{}, firstName:{}, lastName:{}, middleName:{}, email:{}, birthdate:{}, passportSeries:{}, passportNumber:{}}",
                loanApplication.getAmount(), loanApplication.getTerm(), loanApplication.getFirstName(), loanApplication.getLastName(), loanApplication.getMiddleName(), loanApplication.getEmail(), loanApplication.getBirthdate(), loanApplication.getPassportSeries(), loanApplication.getPassportNumber());

        List<LoanOfferDTO> loanOffers = dealService.calculateCreditConditions(loanApplication);

        log.debug("Response postApplication. {}", loanOffers);

        return new ResponseEntity(loanOffers, HttpStatus.OK);
    }

//    @PutMapping("/offer")
//    public ResponseEntity<Void> putOffer(@RequestBody LoanOfferDTO loanOffer) {
//
//    }
//
//    @PutMapping("/calculate/{applicationId}")
//    public ResponseEntity<Void> calculateByAppId(@PathVariable Long applicationId, @RequestBody FinishRegistrationRequestDTO) {
//
//    }
}
