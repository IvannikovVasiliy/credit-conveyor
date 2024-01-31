package com.neoflex.creditconveyer.deal.service.impl;

import com.neoflex.creditconveyer.deal.domain.dto.EmploymentDTO;
import com.neoflex.creditconveyer.deal.domain.dto.FinishRegistrationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.domain.entity.ApplicationEntity;
import com.neoflex.creditconveyer.deal.domain.enumeration.ApplicationStatus;
import com.neoflex.creditconveyer.deal.domain.enumeration.EmploymentStatus;
import com.neoflex.creditconveyer.deal.domain.enumeration.Gender;
import com.neoflex.creditconveyer.deal.error.exception.ApplicationIsPreapprovalException;
import com.neoflex.creditconveyer.deal.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.deal.feign.ConveyorFeignClient;
import com.neoflex.creditconveyer.deal.feign.FeignService;
import com.neoflex.creditconveyer.deal.mapper.SourceMapper;
import com.neoflex.creditconveyer.deal.repository.ApplicationRepository;
import com.neoflex.creditconveyer.deal.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class DealServiceImplTest {

    @Mock
    private SourceMapper sourceMapper;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private ConveyorFeignClient conveyorFeignClient;
    @Mock
    private FeignService feignService;

    @InjectMocks
    DealServiceImpl dealService;

    private BigDecimal requestedAmount = BigDecimal.valueOf(50000);
    private Long applicationId = 44L;

    @Test
    void calculateCreditConditionsTest() {
        LoanApplicationRequestDTO loanApplicationRequest = new LoanApplicationRequestDTO();
        loanApplicationRequest.setAmount(requestedAmount);
        loanApplicationRequest.setTerm(7);
        loanApplicationRequest.setBirthdate(LocalDate.of(2003, 1 ,1));
        loanApplicationRequest.setFirstName("Vasiliy");
        loanApplicationRequest.setLastName("Ivannikov");
        loanApplicationRequest.setPassportSeries("1234");
        loanApplicationRequest.setPassportNumber("123456");

        List<LoanOfferDTO> loanOffersExpected = List.of(
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(50668.8851550000), 7, BigDecimal.valueOf(7238.4121650000), BigDecimal.valueOf(4), true, true),
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(50836.7982850000), 7, BigDecimal.valueOf(7262.3997550000), BigDecimal.valueOf(5), true, false),
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(51511.2074750000), 7, BigDecimal.valueOf(7358.7439250000), BigDecimal.valueOf(9), false, true),
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(51680.4971900000), 7, BigDecimal.valueOf(7382.9281700000), BigDecimal.valueOf(10), false, false)
        );
        List<LoanOfferDTO> loanOffersResponseFeignClient = List.of(
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(50668.8851550000), 7, BigDecimal.valueOf(7238.4121650000), BigDecimal.valueOf(4), true, true),
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(50836.7982850000), 7, BigDecimal.valueOf(7262.3997550000), BigDecimal.valueOf(5), true, false),
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(51511.2074750000), 7, BigDecimal.valueOf(7358.7439250000), BigDecimal.valueOf(9), false, true),
                new LoanOfferDTO(applicationId, requestedAmount, BigDecimal.valueOf(51680.4971900000), 7, BigDecimal.valueOf(7382.9281700000), BigDecimal.valueOf(10), false, false)
        );

        when(feignService.createLoanOffer(loanApplicationRequest)).thenReturn(loanOffersResponseFeignClient);
        List<LoanOfferDTO> loanOffersResponse = dealService.calculateCreditConditions(loanApplicationRequest);
        loanOffersResponse
                .stream()
                .map(loanOffer -> {
                    loanOffer.setApplicationId(applicationId);
                    return loanOffer;
                })
                .toList();

        assertEquals(loanOffersExpected, loanOffersResponse);
    }

    @Test
    void chooseOfferTest() {
        LoanOfferDTO loanOffer = new LoanOfferDTO(applicationId, requestedAmount,
                BigDecimal.valueOf(50668.8851550000), 7,
                BigDecimal.valueOf(7238.4121650000), BigDecimal.valueOf(4), true, true);

        when(applicationRepository.findById(loanOffer.getApplicationId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dealService.chooseOffer(loanOffer));
    }

    @Test
    void finishRegistrationAndCalcAmountCreditTest() {
        EmploymentDTO employment = new EmploymentDTO();
        employment.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        employment.setSalary(BigDecimal.valueOf(20000));
        employment.setWorkExperienceTotal(12);
        employment.setWorkExperienceCurrent(4);

        FinishRegistrationRequestDTO finishRegUnemployedReq = new FinishRegistrationRequestDTO();
        finishRegUnemployedReq.setGender(Gender.MALE);
        finishRegUnemployedReq.setDependentAmount(0);
        finishRegUnemployedReq.setEmployment(employment);
        finishRegUnemployedReq.setAccount("1");

        ApplicationEntity application = new ApplicationEntity();
        application.setStatus(ApplicationStatus.PREAPPROVAL);
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));

        assertThrows(ApplicationIsPreapprovalException.class, () ->
                dealService.finishRegistrationAndCalcAmountCredit(applicationId, finishRegUnemployedReq));
    }
}