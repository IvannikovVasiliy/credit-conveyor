package com.neoflex.creditconveyer.deal.feign;

import com.neoflex.creditconveyer.deal.domain.dto.CreditDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.ScoringDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeignService {

    private final ConveyorFeignClient conveyorFeignClient;

    public List<LoanOfferDTO> createLoanOffer(LoanApplicationRequestDTO loanApplicationRequest) {
        log.debug("Received loanApplicationRequest={ amount:{}, term:{}, firstName:{}, lastName={}, middleName={}, email: {}, birthdate; {}, passportSeries;{}, passportNumber: {} }",
                loanApplicationRequest.getAmount(), loanApplicationRequest.getTerm(), loanApplicationRequest.getFirstName(), loanApplicationRequest.getLastName(), loanApplicationRequest.getMiddleName(), loanApplicationRequest.getEmail(), loanApplicationRequest.getBirthdate(), loanApplicationRequest.getPassportSeries(), loanApplicationRequest.getPassportNumber());

        List<LoanOfferDTO> loanOffers = conveyorFeignClient.calculateOffers(loanApplicationRequest).getBody();

        log.info("Response loanOffers={}", loanOffers);
        return loanOffers;
    }

    public CreditDTO validAndScoreAndCalcOffer(ScoringDataDTO scoringData) {
        log.debug("Received scoring data. scoringData={ {amount:{}, term:{}, firstName:{}, lastName:{}, middleName:{}, gender:{}, birthdate:{}, martialStatus:{}, dependentAmount:{}, employment:{}, account:{},  passportSeries:{}, passportNumber:{}, passportIssueDate:{}, passportIssueBranch:{}, isInsuranceEnabled:{}, isSalaryClient:{}}",
                scoringData.getAmount(), scoringData.getTerm(), scoringData.getFirstName(), scoringData.getLastName(), scoringData.getMiddleName(), scoringData.getGender(), scoringData.getBirthdate(), scoringData.getMartialStatus(), scoringData.getDependentAmount(), scoringData.getEmployment(), scoringData.getAccount(), scoringData.getPassportSeries(), scoringData.getPassportNumber(), scoringData.getPassportIssueDate(), scoringData.getPassportIssueBranch(), scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient());

        CreditDTO credit = conveyorFeignClient.validAndScoreAndCalcOffer(scoringData).getBody();

        log.info("Response creditDTO={ amount: {}, term: {}, monthlyPayment: {}, rate: {}, psk: {}, isInsuranceEnabled: {}, isSalaryClient: {}, paymentSchedule: {} }",
                credit.getAmount(), credit.getTerm(), credit.getMonthlyPayment(), credit.getRate(), credit.getPsk(), credit.getIsInsuranceEnabled(), credit.getIsSalaryClient(), credit.getPaymentSchedule());
        return credit;
    }
}
