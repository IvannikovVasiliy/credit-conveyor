package com.neoflex.creditconveyer.deal.feign;

import com.neoflex.creditconveyer.deal.config.DecoderConfiguration;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "conveyorFeign", url = "${conveyorService.hostPort}/v1/conveyor", configuration = DecoderConfiguration.class)
public interface ConveyorFeignClient {

    @PostMapping("/offers")
    public List<LoanOfferDTO> calculateOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequest);
}
