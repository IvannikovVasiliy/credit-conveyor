package com.neoflex.creditconveyer.deal.feign;

import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
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

        List<LoanOfferDTO> loanOffers = conveyorFeignClient.calculateOffers(loanApplicationRequest);

        log.info("Response loanOffers={}", loanOffers);
        return loanOffers;
    }
}
