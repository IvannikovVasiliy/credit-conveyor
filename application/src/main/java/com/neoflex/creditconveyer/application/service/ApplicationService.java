package com.neoflex.creditconveyer.application.service;

import com.neoflex.creditconveyer.application.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.application.domain.dto.LoanOfferDTO;

import java.util.List;

public interface ApplicationService {
    List<LoanOfferDTO> prescoringAndCalcPossibleConditions(LoanApplicationRequestDTO loanApplication);
}
