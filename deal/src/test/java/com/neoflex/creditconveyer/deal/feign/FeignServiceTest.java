package com.neoflex.creditconveyer.deal.feign;

import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationResponseDto;
import com.neoflex.creditconveyer.deal.error.exception.ConnectionRefusedException;
import feign.RetryableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class FeignServiceTest {

    @Mock
    private ConveyorFeignClient conveyorFeignClient;
    @InjectMocks
    private FeignService feignService;

    @Test
    void createLoanOfferTest() {
//        LoanApplicationRequestDTO loanApplicationRetryableException = new LoanApplicationRequestDTO();
//
//        when(conveyorFeignClient.calculateOffers(loanApplicationRetryableException)).thenThrow(RetryableException.class);
//
//        assertThrows(ConnectionRefusedException.class, () -> feignService.createLoanOffer(loanApplicationRetryableException));
    }
}