package com.neoflex.creditconveyer.application.feign;

import com.neoflex.creditconveyer.application.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.application.error.exception.ConnectionRefusedException;
import feign.RetryableException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealFeignServiceTest {

    @Mock
    private DealFeignClient dealFeignClient;

    @InjectMocks
    private DealFeignService dealFeignService;

    static LoanApplicationRequestDTO loanApplicationForRuntimeException;

    @BeforeAll()
    static void init() {
        loanApplicationForRuntimeException = new LoanApplicationRequestDTO();
    }

    @Test
    void connectionRefusedTest() {
        long applicationIdForRetryableException = -1l;

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