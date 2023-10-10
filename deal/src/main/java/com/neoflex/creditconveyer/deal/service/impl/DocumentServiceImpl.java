package com.neoflex.creditconveyer.deal.service.impl;

import com.neoflex.creditconveyer.deal.domain.constant.Theme;
import com.neoflex.creditconveyer.deal.domain.constant.TopicConstants;
import com.neoflex.creditconveyer.deal.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.deal.domain.entity.ApplicationEntity;
import com.neoflex.creditconveyer.deal.domain.entity.ClientEntity;
import com.neoflex.creditconveyer.deal.error.exception.ResourceNotFoundException;
import com.neoflex.creditconveyer.deal.kafka.producer.EmailProducer;
import com.neoflex.creditconveyer.deal.repository.ApplicationRepository;
import com.neoflex.creditconveyer.deal.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        EmailMessage emailMessage = EmailMessage
                .builder()
                .address(client.getEmail())
                .theme(Theme.SEND_DOCUMENTS)
                .applicationId(applicationId)
                .build();
        emailProducer.sendEmailMessage(TopicConstants.TOPIC_FINISH_REGISTRATION, emailMessage);

        log.debug("Output sendDocuments. Successfully");
    }
}
