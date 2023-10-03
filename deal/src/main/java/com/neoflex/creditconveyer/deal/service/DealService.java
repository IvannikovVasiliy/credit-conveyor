package com.neoflex.creditconveyer.deal.service;

import com.neoflex.creditconveyer.deal.domain.dto.FinishRegistrationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface DealService {
    List<LoanOfferDTO> calculateCreditConditions(@Valid LoanApplicationRequestDTO loanApplicationRequest);
    void chooseOffer(@Valid LoanOfferDTO loanOffer);
    void finishRegistrationAndCalcAmountCredit(@NotNull Long applicationId, FinishRegistrationRequestDTO finishRegistration);
}
