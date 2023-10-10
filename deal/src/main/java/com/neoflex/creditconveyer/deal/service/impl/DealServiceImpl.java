package com.neoflex.creditconveyer.deal.service.impl;

import com.neoflex.creditconveyer.deal.domain.dto.*;
import com.neoflex.creditconveyer.deal.domain.entity.ApplicationEntity;
import com.neoflex.creditconveyer.deal.domain.entity.ClientEntity;
import com.neoflex.creditconveyer.deal.domain.entity.CreditEntity;
import com.neoflex.creditconveyer.deal.domain.enumeration.ApplicationStatus;
import com.neoflex.creditconveyer.deal.domain.enumeration.ChangeType;
import com.neoflex.creditconveyer.deal.domain.enumeration.CreditStatus;
import com.neoflex.creditconveyer.deal.domain.jsonb.EmploymentJsonb;
import com.neoflex.creditconveyer.deal.domain.jsonb.PassportJsonb;
import com.neoflex.creditconveyer.deal.domain.jsonb.StatusHistoryJsonb;
import com.neoflex.creditconveyer.deal.error.exception.ApplicationIsPreapprovalException;
import com.neoflex.creditconveyer.deal.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.deal.feign.FeignService;
import com.neoflex.creditconveyer.deal.mapper.SourceMapper;
import com.neoflex.creditconveyer.deal.repository.ApplicationRepository;
import com.neoflex.creditconveyer.deal.repository.ClientRepository;
import com.neoflex.creditconveyer.deal.repository.CreditRepository;
import com.neoflex.creditconveyer.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class DealServiceImpl implements DealService {

    private final FeignService feignService;
    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;
    private final CreditRepository creditRepository;
    @Qualifier("sourceMapperImplementation") private final SourceMapper sourceMapper;

    @Override
    @Transactional
    public List<LoanOfferDTO> calculateCreditConditions(LoanApplicationRequestDTO loanApplicationRequest) {
        log.debug("Request calculateCreditConditions. loanApplicationRequest={ amount:{}, term:{}, firstName:{}, lastName={}, middleName={}, email: {}, birthdate; {}, passportSeries;{}, passportNumber: {} }",
                loanApplicationRequest.getAmount(), loanApplicationRequest.getTerm(), loanApplicationRequest.getFirstName(), loanApplicationRequest.getLastName(), loanApplicationRequest.getMiddleName(), loanApplicationRequest.getEmail(), loanApplicationRequest.getBirthdate(), loanApplicationRequest.getPassportSeries(), loanApplicationRequest.getPassportNumber());

        ClientEntity client = sourceMapper.sourceToClientEntity(loanApplicationRequest);

        StatusHistoryJsonb statusHistory = new StatusHistoryJsonb();
        statusHistory.setStatus(ApplicationStatus.PREAPPROVAL.name());
        statusHistory.setTime(Timestamp.valueOf(LocalDateTime.now()));
        statusHistory.setChangeType(ChangeType.AUTOMATIC);

        ApplicationEntity application = new ApplicationEntity();
        application.setClient(client);
        application.setStatus(ApplicationStatus.PREAPPROVAL);
        application.setStatusHistory(List.of(statusHistory));

        clientRepository.save(client);
        applicationRepository.save(application);

        List<LoanOfferDTO> loanOffers = createLoanOffers(loanApplicationRequest, application.getId());

        log.info("Response. loanOffers={}", loanOffers);
        return loanOffers;
    }

    @Override
    @Transactional
    public void chooseOffer(LoanOfferDTO loanOffer) {
        log.debug("Request chooseOffer. loanOffer={applicationId: {}, requestedAmount: {}, totalAmount: {}, term: {}, monthlyPayment: {}, rate: {}, isInsuranceEnabled: {}, isSalaryClient: {}}",
                loanOffer.getApplicationId(), loanOffer.getRequestedAmount(), loanOffer.getTotalAmount(), loanOffer.getTerm(), loanOffer.getMonthlyPayment(), loanOffer.getRate(), loanOffer.getIsInsuranceEnabled(), loanOffer.getIsSalaryClient());

        ApplicationEntity application = applicationRepository
                .findById(loanOffer.getApplicationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("Not found. Application with id=%s not found", loanOffer.getApplicationId())));
        application.setStatus(ApplicationStatus.APPROVED);
        List<StatusHistoryJsonb> statusHistories = application.getStatusHistory();
        StatusHistoryJsonb statusHistory = new StatusHistoryJsonb();
        statusHistory.setStatus(ApplicationStatus.APPROVED.name());
        statusHistory.setTime(Timestamp.valueOf(LocalDateTime.now()));
        statusHistory.setChangeType(ChangeType.AUTOMATIC);
        statusHistories.add(statusHistory);
        application.setStatusHistory(statusHistories);
        application.setAppliedOffer(loanOffer);
        applicationRepository.save(application);

        log.info("Response chooseOffer");
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void finishRegistrationAndCalcAmountCredit(Long applicationId, FinishRegistrationRequestDTO finishRegistration) {
        log.debug("Request finishRegistrationAndCalcAmountCredit. applicationId={}, finishRegistration={ gender: {}, martialStatus: {}, dependentAmount: {}, passportIssueDate: {}, passportIssueBranch: {}, employment: { employmentStatus: [}, employerINN: {}, salary: {}, position: {}, workExperienceTotal: {}, workExperienceCurrent: {} } }",
                applicationId, finishRegistration.getGender(), finishRegistration.getMaritalStatus(), finishRegistration.getDependentAmount(), finishRegistration.getPassportIssueDate(), finishRegistration.getPassportIssueBranch(), finishRegistration.getEmployment().getEmploymentStatus(), finishRegistration.getEmployment().getEmployerINN(), finishRegistration.getEmployment().getPosition(), finishRegistration.getEmployment().getWorkExperienceTotal(), finishRegistration.getEmployment().getWorkExperienceCurrent());

        ApplicationEntity application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("Not found. Application with id=%s not found", applicationId)));

        if (ApplicationStatus.PREAPPROVAL.equals(application.getStatus())) {
            log.error("Status application is {}", ApplicationStatus.PREAPPROVAL);
            throw new ApplicationIsPreapprovalException(String.format("Status application is %s", ApplicationStatus.PREAPPROVAL));
        }

        EmploymentJsonb employmentJsonb = sourceMapper.sourceToEmploymentJsonb(finishRegistration.getEmployment());

        ClientEntity clientEntity = application.getClient();
        setValuesIntoClientEntity(clientEntity, finishRegistration, employmentJsonb);

        ScoringDataDTO scoringData = buildScoringData(application, clientEntity, finishRegistration);

        CreditDTO creditDto = feignService.validAndScoreAndCalcOffer(scoringData);

        CreditEntity creditEntity = sourceMapper.sourceToCreditEntity(creditDto);
        creditEntity.setCreditStatus(CreditStatus.CALCULATED);
        creditEntity.setApplication(application);

        List<StatusHistoryJsonb> statusHistories = application.getStatusHistory();
        StatusHistoryJsonb statusHistory = new StatusHistoryJsonb();
        statusHistory.setStatus(ApplicationStatus.CC_APPROVED.name());
        statusHistory.setTime(Timestamp.valueOf(LocalDateTime.now()));
        statusHistory.setChangeType(ChangeType.MANUAL);
        statusHistories.add(statusHistory);

        application.setSignDate(Timestamp.valueOf(LocalDateTime.now()));
        application.setCredit(creditEntity);
        application.setStatus(ApplicationStatus.CC_APPROVED);
        application.setStatusHistory(statusHistories);

        clientRepository.save(clientEntity);
        applicationRepository.save(application);
        creditRepository.save(creditEntity);

        log.info("Response finishRegistrationAndCalcAmountCredit");
    }

    private List<LoanOfferDTO> createLoanOffers(LoanApplicationRequestDTO loanApplicationRequest, Long applicationId) {
        log.debug("Input createLoanOffers. applicationId={}. loanApplicationRequest={ amount:{}, term:{}, firstName:{}, lastName={}, middleName={}, email: {}, birthdate; {}, passportSeries;{}, passportNumber: {} }",
                applicationId, loanApplicationRequest.getAmount(), loanApplicationRequest.getTerm(), loanApplicationRequest.getFirstName(), loanApplicationRequest.getLastName(), loanApplicationRequest.getMiddleName(), loanApplicationRequest.getEmail(), loanApplicationRequest.getBirthdate(), loanApplicationRequest.getPassportSeries(), loanApplicationRequest.getPassportNumber());

        List<LoanOfferDTO> loanOffers = feignService.createLoanOffer(loanApplicationRequest);

        loanOffers = loanOffers
                .stream()
                .peek(loanOfferDTO -> loanOfferDTO.setApplicationId(applicationId))
                .toList();

        log.info("Output createLoanOffers. loanOffers={}", loanOffers);
        return loanOffers;
    }

    private void setValuesIntoClientEntity(ClientEntity clientEntity,
                                           FinishRegistrationRequestDTO finishRegistration,
                                           EmploymentJsonb employmentJsonb) {
        clientEntity.setGender(finishRegistration.getGender());
        clientEntity.setDependentAmount(finishRegistration.getDependentAmount());
        clientEntity.setEmployment(employmentJsonb);
        clientEntity.setAccount(finishRegistration.getAccount());
    }

    private ScoringDataDTO buildScoringData(ApplicationEntity application,
                                            ClientEntity clientEntity,
                                            FinishRegistrationRequestDTO finishRegistration) {
        return ScoringDataDTO
                .builder()
                .amount(application.getAppliedOffer().getRequestedAmount())
                .term(application.getAppliedOffer().getTerm())
                .firstName(clientEntity.getFirstName())
                .lastName(clientEntity.getLastName())
                .middleName(clientEntity.getMiddleName())
                .gender(finishRegistration.getGender())
                .birthdate(clientEntity.getBirthdate().toLocalDate())
                .passportSeries(clientEntity.getPassport().getSeries())
                .passportNumber(clientEntity.getPassport().getNumber())
                .passportIssueDate(finishRegistration.getPassportIssueDate())
                .passportIssueBranch(finishRegistration.getPassportIssueBranch())
                .martialStatus(finishRegistration.getMaritalStatus())
                .dependentAmount(finishRegistration.getDependentAmount())
                .employment(finishRegistration.getEmployment())
                .account(finishRegistration.getAccount())
                .isInsuranceEnabled(application.getAppliedOffer().getIsInsuranceEnabled())
                .isSalaryClient(application.getAppliedOffer().getIsSalaryClient())
                .build();
    }
}
