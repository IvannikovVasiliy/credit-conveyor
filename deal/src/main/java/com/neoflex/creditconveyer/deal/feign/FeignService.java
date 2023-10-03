package com.neoflex.creditconveyer.deal.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoflex.creditconveyer.deal.domain.dto.CreditDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.domain.dto.ScoringDataDTO;
import com.neoflex.creditconveyer.deal.error.exception.ValidationAndScoringAndCalculationOfferException;
import com.neoflex.creditconveyer.deal.error.validation.Violation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public CreditDTO validAndScoreAndCalcOffer(ScoringDataDTO scoringData)  {
        log.debug("Received scoring data. scoringData={ {amount:{}, term:{}, firstName:{}, lastName:{}, middleName:{}, gender:{}, birthdate:{}, martialStatus:{}, dependentAmount:{}, employment:{}, account:{},  passportSeries:{}, passportNumber:{}, passportIssueDate:{}, passportIssueBranch:{}, isInsuranceEnabled:{}, isSalaryClient:{}}",
                scoringData.getAmount(), scoringData.getTerm(), scoringData.getFirstName(), scoringData.getLastName(), scoringData.getMiddleName(), scoringData.getGender(), scoringData.getBirthdate(), scoringData.getMartialStatus(), scoringData.getDependentAmount(), scoringData.getEmployment(), scoringData.getAccount(), scoringData.getPassportSeries(), scoringData.getPassportNumber(), scoringData.getPassportIssueDate(), scoringData.getPassportIssueBranch(), scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient());

        CreditDTO creditResponse = conveyorFeignClient.validAndScoreAndCalcOffer(scoringData).getBody();

//        try {
//            List<Violation> violations = new ArrayList<>();
//            var violationsResponse = (Map<String, List<Map<String, String>>>) credit;
//            violationsResponse
//                    .entrySet()
//                    .iterator()
//                    .next()
//                    .getValue()
//                    .forEach(map -> {
//                        String fieldName = map.get("fieldName");
//                        String message = map.get("message");
//                        Violation violation = new Violation(fieldName, message);
//                        violations.add(violation);
//                    });
//
//            log.debug("Error validAndScoreAndCalcOffer. violations:{}", violations);
//            throw new ValidationAndScoringAndCalculationOfferException(violations);
//        } catch (RuntimeException e) {
//            Map<String, Object> mapResponse = (Map<String, Object>) credit;
//            creditResponse.setAmount((BigDecimal) mapResponse.get("amount"));
//            creditResponse.setTerm((Integer) mapResponse.get("term"));
//            creditResponse.setMonthlyPayment((BigDecimal) mapResponse.get("monthlyPayment"));
//            creditResponse.setRate((BigDecimal) mapResponse.get("rate"));
//            creditResponse.setPsk((BigDecimal) mapResponse.get("psk"));
//            creditResponse.setIsInsuranceEnabled((Boolean) mapResponse.get("isInsuranceEnabled"));
//        }

        log.info("Response creditDTO={ amount: {}, term: {}, monthlyPayment: {}, rate: {}, psk: {}, isInsuranceEnabled: {}, isSalaryClient: {}, paymentSchedule: {} }",
                creditResponse.getAmount(), creditResponse.getTerm(), creditResponse.getMonthlyPayment(), creditResponse.getRate(), creditResponse.getPsk(), creditResponse.getIsInsuranceEnabled(), creditResponse.getIsSalaryClient(), creditResponse.getPaymentSchedule());
        return creditResponse;
    }
}
