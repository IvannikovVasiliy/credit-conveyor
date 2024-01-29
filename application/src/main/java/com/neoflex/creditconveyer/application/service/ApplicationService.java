package com.neoflex.creditconveyer.application.service;

import com.neoflex.creditconveyer.application.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.application.domain.dto.LoanApplicationResponseDTO;
import com.neoflex.creditconveyer.application.domain.dto.LoanOfferDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface ApplicationService {
    LoanApplicationResponseDTO getApplicationById(@NotNull Long applicationId);
    List<LoanOfferDTO> prescoringAndCalcPossibleConditions(@Valid LoanApplicationRequestDTO loanApplication);
    void chooseOffer(@Valid LoanOfferDTO loanOffer);
}
