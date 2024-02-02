package com.neoflex.creditconveyer.deal.service.impl;

import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationResponseDto;
import com.neoflex.creditconveyer.deal.domain.entity.ApplicationEntity;
import com.neoflex.creditconveyer.deal.domain.entity.ClientEntity;
import com.neoflex.creditconveyer.deal.domain.entity.CreditEntity;
import com.neoflex.creditconveyer.deal.domain.enumeration.ApplicationStatus;
import com.neoflex.creditconveyer.deal.domain.jsonb.PassportJsonb;
import com.neoflex.creditconveyer.deal.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.deal.repository.ApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AdminServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void getApplicationByIdTest() {
        Long applicationId = 1L;
        Long applicationThrown = -1L;
        long birthdateUTC = 1_000_000_000_000L;

        PassportJsonb passport = new PassportJsonb();
        passport.setSeries("1234");
        passport.setNumber("123456");

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setVersion(1L);
        clientEntity.setId(1L);
        clientEntity.setLastName("Ivanov");
        clientEntity.setFirstName("Ivan");
        clientEntity.setBirthdate(new Date(birthdateUTC));
        clientEntity.setPassport(passport);

        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setId(applicationId);
        applicationEntity.setVersion(1L);
        applicationEntity.setStatus(ApplicationStatus.PREAPPROVAL);
        applicationEntity.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        applicationEntity.setClient(clientEntity);
        applicationEntity.setCredit(new CreditEntity());

        LoanApplicationResponseDto loanApplicationExpectedResponse = LoanApplicationResponseDto
                .builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .birthdate(new Date(birthdateUTC).toLocalDate())
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(applicationEntity));
        when(applicationRepository.findById(applicationThrown)).thenThrow(ResourceNotFoundException.class);

        LoanApplicationResponseDto loanApplicationResultResponse = adminService.getApplicationById(applicationId);

        assertEquals(loanApplicationExpectedResponse, loanApplicationResultResponse);
        assertThrows(
                ResourceNotFoundException.class,
                () -> adminService.getApplicationById(applicationThrown)
        );
    }
}