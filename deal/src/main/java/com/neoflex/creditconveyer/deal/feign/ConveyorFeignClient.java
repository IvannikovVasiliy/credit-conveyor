package com.neoflex.creditconveyer.deal.feign;

import com.neoflex.creditconveyer.deal.config.DecoderConfiguration;
import com.neoflex.creditconveyer.deal.domain.dto.CreditDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.domain.dto.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "conveyorFeign", url = "${conveyorService.hostPort}/v1/conveyor", configuration = DecoderConfiguration.class)
public interface ConveyorFeignClient {

    @PostMapping("/offers")
    ResponseEntity<List<LoanOfferDTO>> calculateOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequest);

    @PostMapping("/calculation")
    ResponseEntity<CreditDTO> validAndScoreAndCalcOffer(ScoringDataDTO scoringData);
}
