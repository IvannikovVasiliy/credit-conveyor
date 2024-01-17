package com.neoflex.creditconveyer.application.feign;

import com.neoflex.creditconveyer.application.config.DecoderConfiguration;
import com.neoflex.creditconveyer.application.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.application.domain.dto.LoanApplicationResponseDTO;
import com.neoflex.creditconveyer.application.domain.dto.LoanOfferDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRecoveryCallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "dealFeign", url = "${dealService.hostPort}", configuration = DecoderConfiguration.class)
public interface DealFeignClient {
    @GetMapping("/v2/deal/admin/application/{applicationId}")
    ResponseEntity<LoanApplicationResponseDTO> getApplicationById(@Valid @PathVariable Long applicationId);
    @PostMapping("/v1/deal/application")
    ResponseEntity<List<LoanOfferDTO>> calculateOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequest);
    @PutMapping("/v2/deal/offer")
    ResponseEntity<Void> postOffer(@Valid @RequestBody LoanOfferDTO loanOffer);
}