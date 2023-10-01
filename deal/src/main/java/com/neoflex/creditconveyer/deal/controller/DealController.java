package com.neoflex.creditconveyer.deal.controller;

import com.neoflex.creditconveyer.deal.domain.dto.FinishRegistrationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/deal")
public class DealController {

    private final DealService dealService;

    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> postApplication(@RequestBody LoanApplicationRequestDTO loanApplication) {

    }

    @PutMapping("/offer")
    public ResponseEntity<Void> putOffer(@RequestBody LoanOfferDTO loanOffer) {

    }

    @PutMapping("/calculate/{applicationId}")
    public ResponseEntity<Void> calculateByAppId(@PathVariable Long applicationId, @RequestBody FinishRegistrationRequestDTO) {

    }
}
