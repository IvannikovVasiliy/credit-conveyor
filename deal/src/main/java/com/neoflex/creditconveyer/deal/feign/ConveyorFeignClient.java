package com.neoflex.creditconveyer.deal.feign;

import com.neoflex.creditconveyer.deal.config.DecoderConfiguration;
import com.neoflex.creditconveyer.deal.domain.dto.CreditDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.domain.dto.ScoringDataDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "conveyorFeign", url = "${conveyorService.hostPort}/v1/conveyor", configuration = DecoderConfiguration.class)
@Validated
public interface ConveyorFeignClient {

    @PostMapping("/offers")
    ResponseEntity<List<LoanOfferDTO>> calculateOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequest);

    @PostMapping("/calculation")
    ResponseEntity<CreditDTO> validAndScoreAndCalcOffer(@Valid @RequestBody ScoringDataDTO scoringData);
}
