package com.neoflex.creditconveyer.application.feign;

import com.neoflex.creditconveyer.application.config.DecoderConfiguration;
import com.neoflex.creditconveyer.application.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.application.domain.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "dealFeign", url = "${dealService.hostPort}/v1/deal", configuration = DecoderConfiguration.class)
public interface DealFeignClient {

    @PostMapping("/application")
    ResponseEntity<List<LoanOfferDTO>> calculateOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequest);
    @PutMapping("/offer")
    ResponseEntity<Void> postOffer(@RequestBody LoanOfferDTO loanOffer);
}