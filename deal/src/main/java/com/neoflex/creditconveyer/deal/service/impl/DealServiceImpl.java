package com.neoflex.creditconveyer.deal.service.impl;

import com.neoflex.creditconveyer.deal.domain.dto.*;
import com.neoflex.creditconveyer.deal.domain.entity.ApplicationEntity;
import com.neoflex.creditconveyer.deal.domain.entity.ClientEntity;
import com.neoflex.creditconveyer.deal.domain.entity.CreditEntity;
import com.neoflex.creditconveyer.deal.domain.enumeration.ApplicationStatus;
import com.neoflex.creditconveyer.deal.domain.enumeration.ChangeType;
import com.neoflex.creditconveyer.deal.domain.enumeration.CreditStatus;
import com.neoflex.creditconveyer.deal.domain.jsonb.PassportJsonb;
import com.neoflex.creditconveyer.deal.domain.jsonb.PaymentScheduleElementJsonb;
import com.neoflex.creditconveyer.deal.domain.jsonb.StatusHistoryJsonb;
import com.neoflex.creditconveyer.deal.error.exception.ApplicationIsPreapprovalException;
import com.neoflex.creditconveyer.deal.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.deal.feign.FeignService;
import com.neoflex.creditconveyer.deal.repository.ApplicationRepository;
import com.neoflex.creditconveyer.deal.repository.ClientRepository;
import com.neoflex.creditconveyer.deal.repository.CreditRepository;
import com.neoflex.creditconveyer.deal.service.DealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.sql.Date;
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

    @Override
    @Transactional
    public List<LoanOfferDTO> calculateCreditConditions(LoanApplicationRequestDTO loanApplicationRequest) {
        log.debug("Request calculateCreditConditions. loanApplicationRequest={ amount:{}, term:{}, firstName:{}, lastName={}, middleName={}, email: {}, birthdate; {}, passportSeries;{}, passportNumber: {} }",
                loanApplicationRequest.getAmount(), loanApplicationRequest.getTerm(), loanApplicationRequest.getFirstName(), loanApplicationRequest.getLastName(), loanApplicationRequest.getMiddleName(), loanApplicationRequest.getEmail(), loanApplicationRequest.getBirthdate(), loanApplicationRequest.getPassportSeries(), loanApplicationRequest.getPassportNumber());

        PassportJsonb passport = new PassportJsonb();
        passport.setSeries(loanApplicationRequest.getPassportSeries());
        passport.setNumber(loanApplicationRequest.getPassportNumber());

        ClientEntity client = new ClientEntity();
        client.setLastName(loanApplicationRequest.getLastName());
        client.setFirstName(loanApplicationRequest.getFirstName());
        client.setMiddleName(loanApplicationRequest.getMiddleName());
        client.setBirthdate(Date.valueOf(loanApplicationRequest.getBirthdate()));
        client.setEmail(loanApplicationRequest.getEmail());
        client.setBirthdate(Date.valueOf(loanApplicationRequest.getBirthdate()));
        client.setPassport(passport);

        ApplicationEntity application = new ApplicationEntity();
        application.setClient(client);
        application.setStatus(ApplicationStatus.PREAPPROVAL);
        application.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));

        StatusHistoryJsonb statusHistory = new StatusHistoryJsonb();
        statusHistory.setStatus(ApplicationStatus.PREAPPROVAL.name());
        statusHistory.setTime(Timestamp.valueOf(LocalDateTime.now()));
        statusHistory.setChangeType(ChangeType.AUTOMATIC);
        application.setStatusHistory(List.of(statusHistory));

        clientRepository.save(client);
        applicationRepository.save(application);

        List<LoanOfferDTO> loanOffers = new ArrayList<>();
        try {
            loanOffers = feignService.createLoanOffer(loanApplicationRequest);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        loanOffers = loanOffers
                .stream()
                .peek(loanOfferDTO -> loanOfferDTO.setApplicationId(application.getId()))
                .toList();

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

        ClientEntity client = application.getClient();

        ScoringDataDTO scoringData = ScoringDataDTO
                .builder()
                .amount(application.getAppliedOffer().getRequestedAmount())
                .term(application.getAppliedOffer().getTerm())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .gender(finishRegistration.getGender())
                .birthdate(client.getBirthdate().toLocalDate())
                .passportSeries(client.getPassport().getSeries())
                .passportNumber(client.getPassport().getNumber())
                .passportIssueDate(finishRegistration.getPassportIssueDate())
                .passportIssueBranch(finishRegistration.getPassportIssueBranch())
                .martialStatus(finishRegistration.getMaritalStatus())
                .dependentAmount(finishRegistration.getDependentAmount())
                .employment(finishRegistration.getEmployment())
                .account(finishRegistration.getAccount())
                .isInsuranceEnabled(application.getAppliedOffer().getIsInsuranceEnabled())
                .isSalaryClient(application.getAppliedOffer().getIsSalaryClient())
                .build();

        CreditDTO creditDto = feignService.validAndScoreAndCalcOffer(scoringData);
        List<PaymentScheduleElementJsonb> payments = buildPaymentScheduleElementJsonb(creditDto.getPaymentSchedule());

        CreditEntity credit = new CreditEntity();
        credit.setAmount(creditDto.getAmount());
        credit.setTerm(creditDto.getTerm());
        credit.setMonthlyPayment(creditDto.getMonthlyPayment());
        credit.setRate(creditDto.getRate());
        credit.setPsk(creditDto.getPsk());
        credit.setPaymentSchedule(payments);
        credit.setInsuranceEnable(creditDto.getIsInsuranceEnabled());
        credit.setSalaryClient(creditDto.getIsSalaryClient());
        if (null == creditDto.getIsInsuranceEnabled()) {
            credit.setInsuranceEnable(false);
        } else {
            credit.setInsuranceEnable(creditDto.getIsInsuranceEnabled());
        }
        if (null == creditDto.getIsSalaryClient()) {
            credit.setSalaryClient(false);
        } else {
            credit.setSalaryClient(creditDto.getIsSalaryClient());
        }
        credit.setCreditStatus(CreditStatus.CALCULATED);
        credit.setApplication(application);
        creditRepository.save(credit);

        log.info("Response finishRegistrationAndCalcAmountCredit");
    }

    private List<PaymentScheduleElementJsonb> buildPaymentScheduleElementJsonb(List<PaymentScheduleElement> payments) {
        log.debug("Input mapping PaymentScheduleElement into PaymentScheduleElementJsonb. payments={}", payments);

        payments.stream().map(payment -> {
            PaymentScheduleElementJsonb  paymentScheduleElementJsonb = new PaymentScheduleElementJsonb();
            paymentScheduleElementJsonb.setNumber(payment.getNumber());
            paymentScheduleElementJsonb.setDate(payment.getDate());
        })
    }
}
