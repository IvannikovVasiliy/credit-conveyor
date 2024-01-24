package com.neoflex.creditconveyer.deal.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.creditconveyer.deal.domain.dto.CreditDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.domain.dto.ScoringDataDTO;
import com.neoflex.creditconveyer.deal.domain.enumeration.Theme;
import com.neoflex.creditconveyer.deal.error.exception.ConnectionRefusedException;
import com.neoflex.creditconveyer.deal.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyer.deal.error.validation.Violation;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeignService {

    private final ConveyorFeignClient conveyorFeignClient;

    @Value("${conveyorService.hostPort}")
    private String CONVEYOR_HOST_PORT;

    public List<LoanOfferDTO> createLoanOffer(LoanApplicationRequestDTO loanApplicationRequest) {
        log.debug("Received loanApplicationRequest={ amount:{}, term:{}, firstName:{}, lastName={}, middleName={}, email: {}, birthdate; {}, passportSeries;{}, passportNumber: {} }",
                loanApplicationRequest.getAmount(), loanApplicationRequest.getTerm(), loanApplicationRequest.getFirstName(), loanApplicationRequest.getLastName(), loanApplicationRequest.getMiddleName(), loanApplicationRequest.getEmail(), loanApplicationRequest.getBirthdate(), loanApplicationRequest.getPassportSeries(), loanApplicationRequest.getPassportNumber());

        try {
            List<LoanOfferDTO> loanOffers = conveyorFeignClient.calculateOffers(loanApplicationRequest).getBody();
            log.info("Response loanOffers={}", loanOffers);
            return loanOffers;
        } catch (RetryableException exception) {
            throw new ConnectionRefusedException(
                    String.format("Connection refused. No further information %s", CONVEYOR_HOST_PORT)
            );
        }
    }

    public CreditDTO validAndScoreAndCalcOffer(ScoringDataDTO scoringData) {
        log.debug("Received scoring data. scoringData={ {amount:{}, term:{}, firstName:{}, lastName:{}, middleName:{}, gender:{}, birthdate:{}, martialStatus:{}, dependentAmount:{}, employment:{}, account:{},  passportSeries:{}, passportNumber:{}, passportIssueDate:{}, passportIssueBranch:{}, isInsuranceEnabled:{}, isSalaryClient:{}}",
                scoringData.getAmount(), scoringData.getTerm(), scoringData.getFirstName(), scoringData.getLastName(), scoringData.getMiddleName(), scoringData.getGender(), scoringData.getBirthdate(), scoringData.getMartialStatus(), scoringData.getDependentAmount(), scoringData.getEmployment(), scoringData.getAccount(), scoringData.getPassportSeries(), scoringData.getPassportNumber(), scoringData.getPassportIssueDate(), scoringData.getPassportIssueBranch(), scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient());

        CreditDTO creditResponse = null;
        creditResponse = conveyorFeignClient.validAndScoreAndCalcOffer(scoringData).getBody();

        log.info("Response creditDTO={ amount: {}, term: {}, monthlyPayment: {}, rate: {}, psk: {}, isInsuranceEnabled: {}, isSalaryClient: {}, paymentSchedule: {} }",
                creditResponse.getAmount(), creditResponse.getTerm(), creditResponse.getMonthlyPayment(), creditResponse.getRate(), creditResponse.getPsk(), creditResponse.getIsInsuranceEnabled(), creditResponse.getIsSalaryClient(), creditResponse.getPaymentSchedule());
        return creditResponse;
    }
}
