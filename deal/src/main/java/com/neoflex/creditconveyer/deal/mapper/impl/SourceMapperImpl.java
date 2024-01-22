package com.neoflex.creditconveyer.deal.mapper.impl;

import com.neoflex.creditconveyer.deal.domain.dto.CreditDTO;
import com.neoflex.creditconveyer.deal.domain.dto.EmploymentDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.PaymentScheduleElement;
import com.neoflex.creditconveyer.deal.domain.entity.ApplicationEntity;
import com.neoflex.creditconveyer.deal.domain.entity.ClientEntity;
import com.neoflex.creditconveyer.deal.domain.entity.CreditEntity;
import com.neoflex.creditconveyer.deal.domain.jsonb.EmploymentJsonb;
import com.neoflex.creditconveyer.deal.domain.jsonb.PassportJsonb;
import com.neoflex.creditconveyer.deal.domain.jsonb.PaymentScheduleElementJsonb;
import com.neoflex.creditconveyer.deal.domain.jsonb.StatusHistoryJsonb;
import com.neoflex.creditconveyer.deal.domain.model.ApplicationModel;
import com.neoflex.creditconveyer.deal.domain.model.ClientModel;
import com.neoflex.creditconveyer.deal.domain.model.CreditModel;
import com.neoflex.creditconveyer.deal.mapper.SourceMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component("sourceMapperImplementation")
public class SourceMapperImpl implements SourceMapper {

    @Override
    public ClientEntity sourceToClientEntity(LoanApplicationRequestDTO loanApplicationRequest) {
        if ( loanApplicationRequest == null ) {
            return null;
        }

        ClientEntity clientEntity = new ClientEntity();

        clientEntity.setLastName( loanApplicationRequest.getLastName() );
        clientEntity.setFirstName( loanApplicationRequest.getFirstName() );
        clientEntity.setMiddleName( loanApplicationRequest.getMiddleName() );
        if ( loanApplicationRequest.getBirthdate() != null ) {
            clientEntity.setBirthdate(new Date(
                    loanApplicationRequest
                            .getBirthdate()
                            .atStartOfDay(ZoneOffset.UTC)
                            .toInstant()
                            .toEpochMilli()
            ));
        }
        clientEntity.setEmail( loanApplicationRequest.getEmail() );

        PassportJsonb passport = new PassportJsonb();
        passport.setSeries(loanApplicationRequest.getPassportSeries());
        passport.setNumber(loanApplicationRequest.getPassportNumber());
        clientEntity.setPassport(passport);

        return clientEntity;
    }

    @Override
    public CreditEntity sourceToCreditEntity(CreditDTO creditDTO) {
        if ( creditDTO == null ) {
            return null;
        }

        CreditEntity creditEntity = new CreditEntity();

        creditEntity.setAmount( creditDTO.getAmount() );
        creditEntity.setTerm( creditDTO.getTerm() );
        creditEntity.setMonthlyPayment( creditDTO.getMonthlyPayment() );
        creditEntity.setRate( creditDTO.getRate() );
        creditEntity.setPsk( creditDTO.getPsk() );

        creditEntity.setInsuranceEnable(creditDTO.getIsInsuranceEnabled());
        creditEntity.setSalaryClient(creditDTO.getIsSalaryClient());

        creditEntity.setPaymentSchedule( paymentListToPaymentJsonbList( creditDTO.getPaymentSchedule() ) );

        return creditEntity;
    }

    @Override
    public EmploymentJsonb sourceToEmploymentJsonb(EmploymentDTO employmentDTO) {
        if ( employmentDTO == null ) {
            return null;
        }

        EmploymentJsonb employmentJsonb = new EmploymentJsonb();

        employmentJsonb.setStatus(employmentDTO.getEmploymentStatus());
        employmentJsonb.setEmployerInn(employmentJsonb.getEmployerInn());
        employmentJsonb.setSalary( employmentDTO.getSalary() );
        employmentJsonb.setPosition( employmentDTO.getPosition() );
        employmentJsonb.setWorkExperienceTotal( employmentDTO.getWorkExperienceTotal() );
        employmentJsonb.setWorkExperienceCurrent( employmentDTO.getWorkExperienceCurrent() );

        return employmentJsonb;
    }

    @Override
    public ClientModel sourceToClientModel(ClientEntity clientEntity) {
        if ( clientEntity == null ) {
            return null;
        }

        ClientModel clientModel = new ClientModel();

        clientModel.setLastName( clientEntity.getLastName() );
        clientModel.setFirstName( clientEntity.getFirstName() );
        clientModel.setMiddleName( clientEntity.getMiddleName() );
        clientModel.setBirthdate( clientEntity.getBirthdate() );
        clientModel.setEmail( clientEntity.getEmail() );
        clientModel.setMartialStatus( clientEntity.getMartialStatus() );
        clientModel.setDependentAmount( clientEntity.getDependentAmount() );
        clientModel.setPassport( clientEntity.getPassport() );
        clientModel.setEmployment( clientEntity.getEmployment() );
        clientModel.setAccount( clientEntity.getAccount() );

        return clientModel;
    }

    @Override
    public ApplicationModel sourceToApplicationModel(ApplicationEntity applicationEntity) {
        if ( applicationEntity == null ) {
            return null;
        }

        ApplicationModel applicationModel = new ApplicationModel();

        applicationModel.setStatus( applicationEntity.getStatus() );
        applicationModel.setCreationDate( applicationEntity.getCreationDate() );
        applicationModel.setAppliedOffer( applicationEntity.getAppliedOffer() );
        List<StatusHistoryJsonb> list = applicationEntity.getStatusHistory();
        if ( list != null ) {
            applicationModel.setStatusHistory( new ArrayList<StatusHistoryJsonb>( list ) );
        }

        return applicationModel;
    }

    @Override
    public CreditModel sourceToCreditModel(CreditEntity creditEntity) {
        if ( creditEntity == null ) {
            return null;
        }

        CreditModel creditModel = new CreditModel();

        creditModel.setAmount( creditEntity.getAmount() );
        creditModel.setTerm( creditEntity.getTerm() );
        creditModel.setMonthlyPayment( creditEntity.getMonthlyPayment() );
        creditModel.setRate( creditEntity.getRate() );
        creditModel.setPsk( creditEntity.getPsk() );
        List<PaymentScheduleElementJsonb> list = creditEntity.getPaymentSchedule();
        if ( list != null ) {
            creditModel.setPaymentSchedule( new ArrayList<PaymentScheduleElementJsonb>( list ) );
        }
        creditModel.setInsuranceEnable( creditEntity.getInsuranceEnable() );
        creditModel.setSalaryClient( creditEntity.getSalaryClient() );
        creditModel.setCreditStatus( creditEntity.getCreditStatus() );

        return creditModel;
    }

    protected PaymentScheduleElementJsonb paymentToPaymentJsonb(PaymentScheduleElement paymentScheduleElement) {
        if ( paymentScheduleElement == null ) {
            return null;
        }

        PaymentScheduleElementJsonb paymentScheduleElementJsonb = new PaymentScheduleElementJsonb();

        paymentScheduleElementJsonb.setNumber( paymentScheduleElement.getNumber() );
        if ( paymentScheduleElement.getDate() != null ) {
            paymentScheduleElementJsonb.setDate( new Date( paymentScheduleElement.getDate().atStartOfDay( ZoneOffset.UTC ).toInstant().toEpochMilli() ) );
        }
        paymentScheduleElementJsonb.setTotalPayment( paymentScheduleElement.getTotalPayment() );
        paymentScheduleElementJsonb.setInterestPayment( paymentScheduleElement.getInterestPayment() );
        paymentScheduleElementJsonb.setDebtPayment( paymentScheduleElement.getDebtPayment() );
        paymentScheduleElementJsonb.setRemainingDebt( paymentScheduleElement.getRemainingDebt() );

        return paymentScheduleElementJsonb;
    }

    protected List<PaymentScheduleElementJsonb> paymentListToPaymentJsonbList(List<PaymentScheduleElement> list) {
        if ( list == null ) {
            return null;
        }

        List<PaymentScheduleElementJsonb> list1 = new ArrayList<>( list.size() );
        for ( PaymentScheduleElement paymentScheduleElement : list ) {
            list1.add( paymentToPaymentJsonb( paymentScheduleElement ) );
        }

        return list1;
    }
}
