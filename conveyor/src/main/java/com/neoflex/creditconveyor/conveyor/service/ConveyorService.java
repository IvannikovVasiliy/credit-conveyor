package com.neoflex.creditconveyor.conveyor.service;

import com.neoflex.creditconveyor.conveyor.domain.dto.CreditDTO;
import com.neoflex.creditconveyor.conveyor.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyor.conveyor.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyor.conveyor.domain.dto.ScoringDataDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface ConveyorService {
    List<LoanOfferDTO> createLoanOffer(@Valid LoanApplicationRequestDTO loanApplicationRequest);
    CreditDTO validAndScoreAndCalcOffer(@Valid ScoringDataDTO scoringData);
}
