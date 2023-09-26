package com.neoflex.creditconveyor.conveyor.service;

import com.neoflex.creditconveyor.conveyor.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyor.conveyor.domain.dto.LoanOfferDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface ConveyorService {
    List<LoanOfferDTO> createLoanOffer(@Valid LoanApplicationRequestDTO loanApplicationRequest);
}
