package com.neoflex.creditconveyer.application.feign;

import com.neoflex.creditconveyer.application.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.application.domain.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealFeignService {

    private final DealFeignClient dealFeignClient;

    public List<LoanOfferDTO> postDealApplication(LoanApplicationRequestDTO loanApplicationRequest) {
        log.debug("Received loanApplicationRequest={ amount:{}, term:{}, firstName:{}, lastName={}, middleName={}, email: {}, birthdate; {}, passportSeries;{}, passportNumber: {} }",
                loanApplicationRequest.getAmount(), loanApplicationRequest.getTerm(), loanApplicationRequest.getFirstName(), loanApplicationRequest.getLastName(), loanApplicationRequest.getMiddleName(), loanApplicationRequest.getEmail(), loanApplicationRequest.getBirthdate(), loanApplicationRequest.getPassportSeries(), loanApplicationRequest.getPassportNumber());

        List<LoanOfferDTO> loanOffers = dealFeignClient.calculateOffers(loanApplicationRequest).getBody();

        log.info("Response loanOffers={}", loanOffers);
        return loanOffers;
    }

    public void putOffer(LoanOfferDTO loanOffer) {
        log.debug("Request putOffer. loanOffer={applicationId: {}, requestedAmount: {}, totalAmount: {}, term: {}, monthlyPayment: {}, rate: {}, isInsuranceEnabled: {}, isSalaryClient: {}}",
                loanOffer.getApplicationId(), loanOffer.getRequestedAmount(), loanOffer.getTotalAmount(), loanOffer.getTerm(), loanOffer.getMonthlyPayment(), loanOffer.getRate(), loanOffer.getIsInsuranceEnabled(), loanOffer.getIsSalaryClient());

        dealFeignClient.postOffer(loanOffer);

        log.debug("Output putOffer");
    }
}
