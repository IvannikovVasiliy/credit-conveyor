package com.neoflex.creditconveyer.deal.service.impl;

import com.neoflex.creditconveyer.deal.domain.dto.LoanApplicationRequestDTO;
import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.domain.entity.ApplicationEntity;
import com.neoflex.creditconveyer.deal.domain.entity.ClientEntity;
import com.neoflex.creditconveyer.deal.domain.jsonb.PassportJsonb;
import com.neoflex.creditconveyer.deal.feign.FeignService;
import com.neoflex.creditconveyer.deal.service.DealService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealServiceImpl implements DealService {

    @PersistenceContext
    private EntityManager entityManager;

    private final FeignService feignService;

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
        entityManager.persist(client);

        ApplicationEntity application = new ApplicationEntity();
        application.setClient(client);
        application.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        entityManager.persist(application);

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
}
