package com.neoflex.creditconveyer.deal.service;

import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;

import java.util.List;

public interface DealService {
    List<LoanOfferDTO> calculateCreditConditions(LoanApplicationRequestDTO loanApplicationRequest);
}
