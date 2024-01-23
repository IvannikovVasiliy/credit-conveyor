package com.neoflex.creditconveyer.deal.service.impl;

import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationResponseDto;
import com.neoflex.creditconveyer.deal.domain.dto.PageDto;
import com.neoflex.creditconveyer.deal.domain.entity.ApplicationEntity;
import com.neoflex.creditconveyer.deal.domain.entity.ClientEntity;
import com.neoflex.creditconveyer.deal.domain.entity.CreditEntity;
import com.neoflex.creditconveyer.deal.domain.enumeration.ApplicationStatus;
import com.neoflex.creditconveyer.deal.domain.jsonb.PassportJsonb;
import com.neoflex.creditconveyer.deal.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.deal.repository.ApplicationRepository;
import com.neoflex.creditconveyer.deal.repository.ClientRepository;
import com.neoflex.creditconveyer.deal.repository.CreditRepository;
import com.neoflex.creditconveyer.deal.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final ApplicationRepository applicationRepository;

    @Override
    public LoanApplicationResponseDto getApplicationById(Long applicationId) {
        log.debug("Input in method getApplicationById for getting application by id. Input parameter applicationId={}",
                applicationId);

        ApplicationEntity applicationEntity = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Application with id=%d not found", applicationId)
                ));
        CreditEntity creditEntity = applicationEntity.getCredit();
        ClientEntity clientEntity = applicationEntity.getClient();
        PassportJsonb passportJson = clientEntity.getPassport();

        LocalDate birthDate = clientEntity
                .getBirthdate()
                .toLocalDate();

        LoanApplicationResponseDto loanApplicationResponseDto = LoanApplicationResponseDto
                .builder()
                .amount(creditEntity.getAmount())
                .term(creditEntity.getTerm())
                .firstName(clientEntity.getFirstName())
                .lastName(clientEntity.getLastName())
                .middleName(clientEntity.getMiddleName())
                .email(clientEntity.getEmail())
                .birthdate(birthDate)
                .passportSeries(passportJson.getSeries())
                .passportNumber(passportJson.getNumber())
                .build();

        log.debug("Build loan-application by applicationId={}", applicationId);

        return loanApplicationResponseDto;
    }

    @Override
    public List<LoanApplicationResponseDto> getAllApplications(PageDto page) {
        return applicationRepository
                .findAll(page.pageable())
                .stream()
                .map(applicationEntity -> {
                    CreditEntity creditEntity = applicationEntity.getCredit();
                    ClientEntity clientEntity = applicationEntity.getClient();
                    PassportJsonb passportJson = clientEntity.getPassport();

                    LocalDate birthDate = clientEntity
                            .getBirthdate()
                            .toLocalDate();

                    return LoanApplicationResponseDto
                            .builder()
                            .amount(creditEntity.getAmount())
                            .term(creditEntity.getTerm())
                            .firstName(clientEntity.getFirstName())
                            .lastName(clientEntity.getLastName())
                            .middleName(clientEntity.getMiddleName())
                            .email(clientEntity.getEmail())
                            .birthdate(birthDate)
                            .passportSeries(passportJson.getSeries())
                            .passportNumber(passportJson.getNumber())
                            .build();
                })
                .toList();
    }

    @Override
    public void updateStatusByApplicationId(Long applicationId) {
        log.debug("Input updateStatusByApplicationId. applicationId={}", applicationId);

        ApplicationEntity applicationEntity = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("Not found. Application with id=%s not found", applicationId)));
        applicationEntity.setStatus(ApplicationStatus.DOCUMENT_CREATED);
        applicationRepository.save(applicationEntity);

        log.debug("Output updateStatusByApplicationId. Success");
    }
}
