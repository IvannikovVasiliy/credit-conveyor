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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/conveyor")
public class ConveyorController {

    private final ConveyorService conveyorService;

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDTO>> calculateOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequest) {
        log.debug("Request calculate offers. loanApplicationRequest={amount: {}, term:{}, firstName:{}, lastName:{}, middleName:{}, email:{}, birthdate:{}, passportSeries:{}, passportNumber:{}}",
                loanApplicationRequest.getAmount(), loanApplicationRequest.getTerm(), loanApplicationRequest.getFirstName(), loanApplicationRequest.getLastName(), loanApplicationRequest.getMiddleName(), loanApplicationRequest.getEmail(), loanApplicationRequest.getBirthdate(), loanApplicationRequest.getPassportSeries(), loanApplicationRequest.getPassportNumber());

        List<LoanOfferDTO> loanOffers = conveyorService.createLoanOffer(loanApplicationRequest);

        log.debug("Response calculate offers. loanOffers={}", loanOffers);
        return new ResponseEntity(loanOffers, HttpStatus.OK);
    }

    @PostMapping("/calculation")
    public ResponseEntity<CreditDTO> validAndScoreAndCalcOffer(@Valid @RequestBody ScoringDataDTO scoringData) {
        log.debug("Request validate datas and score and calculate credit parameters. scoringData: {amount:{}, term:{}, firstName:{}, lastName:{}, middleName:{}, gender:{}, birthdate:{}, martialStatus:{}, dependentAmount:{}, employment:{}, account:{},  passportSeries:{}, passportNumber:{}, passportIssueDate:{}, passportIssueBranch:{}, isInsuranceEnabled:{}, isSalaryClient:{}}",
                scoringData.getAmount(), scoringData.getTerm(), scoringData.getFirstName(), scoringData.getLastName(), scoringData.getMiddleName(), scoringData.getGender(), scoringData.getBirthdate(), scoringData.getMartialStatus(), scoringData.getDependentAmount(), scoringData.getEmployment(), scoringData.getAccount(), scoringData.getPassportSeries(), scoringData.getPassportNumber(), scoringData.getPassportIssueDate(), scoringData.getPassportIssueBranch(), scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient());

        CreditDTO creditDTO = conveyorService.validAndScoreAndCalcOffer(scoringData);

        log.debug("Response. creditDTO: {}", creditDTO);
        return new ResponseEntity<>(creditDTO, HttpStatus.OK);
    }
}
