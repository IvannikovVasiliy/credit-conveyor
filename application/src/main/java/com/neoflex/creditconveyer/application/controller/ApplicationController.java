package com.neoflex.creditconveyer.application.controller;

import com.neoflex.creditconveyer.application.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.application.domain.dto.LoanOfferDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/v1/application")
public class ApplicationController {

//    @PostMapping
//    public ResponseEntity<List<LoanOfferDTO>> postApplication(@RequestBody LoanApplicationRequestDTO loanApplication) {
//        log.debug("Request calculate offers. loanApplication={amount: {}, term:{}, firstName:{}, lastName:{}, middleName:{}, email:{}, birthdate:{}, passportSeries:{}, passportNumber:{}}",
//                loanApplication.getAmount(), loanApplication.getTerm(), loanApplication.getFirstName(), loanApplication.getLastName(), loanApplication.getMiddleName(), loanApplication.getEmail(), loanApplication.getBirthdate(), loanApplication.getPassportSeries(), loanApplication.getPassportNumber());
//
//
//        log.debug("Response calculate offers. loanOffers={}", loanOffers);
//    }
//
//    @PutMapping("/offer")
//    public ResponseEntity<Void> putOffer(@RequestBody LoanOfferDTO loanOffer) {
//
//    }
}
