package com.neoflex.creditconveyer.application.feign;

import com.neoflex.creditconveyer.application.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.application.error.exception.ConnectionRefusedException;
import feign.RetryableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DealFeignServiceTest {

    @Mock
    private DealFeignClient dealFeignClient;

    @InjectMocks
    private DealFeignService dealFeignService;

    @Test
    void connectionRefusedTest() {
        long applicationIdForRetryableException = -1l;
        LoanApplicationRequestDTO loanApplicationForRuntimeException = new LoanApplicationRequestDTO();

        when(dealFeignClient.getApplicationById(applicationIdForRetryableException))
                .thenThrow(RetryableException.class);
        when(dealFeignClient.calculateOffers(loanApplicationForRuntimeException))
                .thenThrow(RuntimeException.class);

        assertThrows(
                ConnectionRefusedException.class,
                () -> dealFeignService.getApplicationById(applicationIdForRetryableException)
        );
        assertThrows(
                RuntimeException.class,
                () -> dealFeignService.postDealApplication(loanApplicationForRuntimeException)
        );
    }
}