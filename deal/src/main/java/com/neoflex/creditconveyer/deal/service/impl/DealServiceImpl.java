package com.neoflex.creditconveyer.deal.service.impl;

import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.domain.entity.ClientEntity;
import com.neoflex.creditconveyer.deal.service.DealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@Slf4j
public class DealServiceImpl implements DealService {

    @Override
    public List<LoanOfferDTO> calculateCreditConditions(LoanApplicationRequestDTO loanApplicationRequest) {
        log.debug("Request calculateCreditConditions. loanApplicationRequest={ amount:{}, term:{}, firstName:{}, lastName={}, middleName={}, email: {}, birthdate; {}, passportSeries;{}, passportNumber: {} }",
                loanApplicationRequest.getAmount(), loanApplicationRequest.getTerm(), loanApplicationRequest.getFirstName(), loanApplicationRequest.getLastName(), loanApplicationRequest.getMiddleName(), loanApplicationRequest.getEmail(), loanApplicationRequest.getBirthdate(), loanApplicationRequest.getPassportSeries(), loanApplicationRequest.getPassportNumber());

        ClientEntity client = new ClientEntity();
        client.setLastName(loanApplicationRequest.getLastName());
        client.setFirstName(loanApplicationRequest.getFirstName());
        client.setMiddleName(loanApplicationRequest.getMiddleName());
        client.setBirthdate(Date.valueOf(loanApplicationRequest.getBirthdate()));
        client.setEmail(loanApplicationRequest.getEmail());
        client.setGender(loanApplicationRequest.getGe);
    }
}
