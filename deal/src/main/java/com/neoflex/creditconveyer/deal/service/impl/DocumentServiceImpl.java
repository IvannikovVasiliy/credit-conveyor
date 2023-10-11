package com.neoflex.creditconveyer.deal.service.impl;

import com.neoflex.creditconveyer.deal.domain.constant.TopicConstants;
import com.neoflex.creditconveyer.deal.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.deal.domain.dto.SesEmailMessage;
import com.neoflex.creditconveyer.deal.domain.dto.VerifyCodeDTO;
import com.neoflex.creditconveyer.deal.domain.entity.ApplicationEntity;
import com.neoflex.creditconveyer.deal.domain.entity.ClientEntity;
import com.neoflex.creditconveyer.deal.domain.enumeration.ApplicationStatus;
import com.neoflex.creditconveyer.deal.domain.enumeration.Theme;
import com.neoflex.creditconveyer.deal.error.exception.ErrorSesCodeException;
import com.neoflex.creditconveyer.deal.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.deal.kafka.producer.EmailProducer;
import com.neoflex.creditconveyer.deal.repository.ApplicationRepository;
import com.neoflex.creditconveyer.deal.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final EmailProducer emailProducer;
    private final ApplicationRepository applicationRepository;

    @Override
    public void sendDocuments(Long applicationId) {
        log.debug("Input sendDocuments. applicationId: {}", applicationId);

        ApplicationEntity application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("Application with id=%d not found", applicationId)));
        ClientEntity client = application.getClient();

        application.setStatus(ApplicationStatus.PREPARE_DOCUMENTS);
        applicationRepository.save(application);

        EmailMessage emailMessage = EmailMessage
                .builder()
                .address(client.getEmail())
                .theme(Theme.SEND_DOCUMENTS)
                .applicationId(applicationId)
                .build();
        emailProducer.sendEmailMessage(TopicConstants.TOPIC_SEND_DOCUMENTS, emailMessage);

        log.debug("Output sendDocuments. Successfully");
    }

    @Override
    public void signDocuments(Long applicationId) {
        log.debug("Input signDocuments. applicationId: {}", applicationId);

        ApplicationEntity application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("Application with id=%d not found", applicationId)));
        Random random = new Random();
        Integer sesCode = random.nextInt(89999999) + 10000000;
        application.setSesCode(sesCode);
        SesEmailMessage sesEmailMessage = SesEmailMessage
                .builder()
                .sesCode(sesCode)
                .address(application.getClient().getEmail())
                .theme(Theme.SIGN_DOCUMENTS)
                .applicationId(applicationId)
                .build();
        emailProducer.sendSesCodeEmailMessage(TopicConstants.TOPIC_SIGN_DOCUMENTS, sesEmailMessage);

        log.debug("Output signDocuments. Success");
    }

    @Override
    public void issuedCredit(Long applicationId, VerifyCodeDTO verifyCodeDTO) {
        log.debug("Input issuedCredit. sesCode: {}", verifyCodeDTO.getSesCode());

        ApplicationEntity application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Not found. Application with id=%s not found", applicationId)));

        if (!application.getSesCode().equals(verifyCodeDTO.getSesCode())) {
            throw new ErrorSesCodeException("Conflict. SES code is wrong");
        }

        // .save(...)

        EmailMessage emailMessage = EmailMessage
                .builder()
                .address(application.getClient().getEmail())
                .theme(Theme.CREDIT_ISSUED)
                .applicationId(applicationId)
                .build();
        emailProducer.sendEmailMessage(TopicConstants.TOPIC_CREDIT_ISSUED, emailMessage);

        log.debug("Output issuedCredit. sesCode: {}", verifyCodeDTO.getSesCode());
    }
}
