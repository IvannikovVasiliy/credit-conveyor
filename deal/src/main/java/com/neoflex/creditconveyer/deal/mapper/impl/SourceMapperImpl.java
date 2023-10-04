package com.neoflex.creditconveyer.deal.mapper.impl;

import com.neoflex.creditconveyer.deal.domain.dto.CreditDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.PaymentScheduleElement;
import com.neoflex.creditconveyer.deal.domain.entity.ClientEntity;
import com.neoflex.creditconveyer.deal.domain.entity.CreditEntity;
import com.neoflex.creditconveyer.deal.domain.jsonb.PassportJsonb;
import com.neoflex.creditconveyer.deal.domain.jsonb.PaymentScheduleElementJsonb;
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
        passport.setPassportId(UUID.randomUUID());
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
