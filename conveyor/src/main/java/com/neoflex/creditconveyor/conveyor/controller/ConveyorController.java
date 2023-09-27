package com.neoflex.creditconveyor.conveyor.controller;

import com.neoflex.creditconveyor.conveyor.domain.dto.CreditDTO;
import com.neoflex.creditconveyor.conveyor.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyor.conveyor.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyor.conveyor.domain.dto.ScoringDataDTO;
import com.neoflex.creditconveyor.conveyor.service.ConveyorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/conveyor")
public class ConveyorController {

    private final ConveyorService conveyorService;

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDTO>> calculateOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequest) {
        log.debug("Request calculate offers. loanApplicationRequest={ amount:{}, term:{}, firstName:{}, lastName={}, middleName={}, email: {}, birthdate; {}, passportSeries;{}, passportNumber: {} }",
                loanApplicationRequest.getAmount(), loanApplicationRequest.getTerm(), loanApplicationRequest.getFirstName(), loanApplicationRequest.getLastName(), loanApplicationRequest.getMiddleName(), loanApplicationRequest.getEmail(), loanApplicationRequest.getBirthdate(), loanApplicationRequest.getPassportSeries(), loanApplicationRequest.getPassportNumber());

        List<LoanOfferDTO> loanOffers = conveyorService.createLoanOffer(loanApplicationRequest);

        log.debug("Response calculate offers. loanOffers={}", loanOffers);
        return new ResponseEntity(loanOffers, HttpStatus.OK);
    }

    @PostMapping("/calculation")
    public ResponseEntity<CreditDTO> validScoreCalcOfferParam(@RequestBody ScoringDataDTO scoringData) {
        return null;
    }
}
