package com.neoflex.creditconveyer.dossier.kafka;

import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessageDto;
import com.neoflex.creditconveyer.dossier.domain.dto.InformationEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.SesEmailMessageDto;
import com.neoflex.creditconveyer.dossier.service.DossierService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
@Slf4j
public class DossierConsumer {

    @Autowired
    public DossierConsumer(
            @Qualifier("createDocumentsKafkaConsumerExecutorService") ExecutorService executorServiceCreateDocuments,
            @Qualifier("sendDocumentsKafkaConsumerExecutorService") ExecutorService executorServiceSendDocuments,
            DossierService dossierService
    ) {
        this.executorServiceCreateDocuments = executorServiceCreateDocuments;
        this.executorServiceSendDocuments = executorServiceSendDocuments;
        this.dossierService = dossierService;
    }

    private final ExecutorService executorServiceCreateDocuments;
    private final ExecutorService executorServiceSendDocuments;
    private final DossierService dossierService;

    @KafkaListener(
            topics = "${kafka.topics.create-documents-topic.name}",
            groupId = "${kafka.group-id}",
            containerFactory = "createDocumentKafkaListener"
    )
    public void createDocumentConsumer(ConsumerRecord<String, InformationEmailMessage> createDocumentRecord,
                                       Acknowledgment acknowledgment) {
        executorServiceCreateDocuments.submit(() -> {
            log.debug("consume message: offset={}, key={} from topic: create-documents",
                    createDocumentRecord.offset(), createDocumentRecord.key());
            dossierService.createDocuments(createDocumentRecord.value());
            acknowledgment.acknowledge();
        });
    }

    @KafkaListener(
            topics = "${kafka.topics.send-documents-topic.name}",
            groupId = "${kafka.group-id}",
            containerFactory = "sendDocumentsKafkaListener"
    )
    public void sendDocumentsConsumer(ConsumerRecord<String, EmailMessageDto> emailMessageRecord,
                                      Acknowledgment acknowledgment) {
        executorServiceSendDocuments.submit(() -> {
            log.debug("consume message: offset={}, key={} from topic: send-documents",
                    emailMessageRecord.offset(), emailMessageRecord.key());
            dossierService.sendDocuments(emailMessageRecord.value());
            acknowledgment.acknowledge();
        });
    }

    @KafkaListener(
            topics = "${kafka.topics.sign-documents-topic.name}",
            groupId = "${kafka.group-id}",
            containerFactory = "signDocumentsKafkaListener"
    )
    public void signDocumentsConsumer(ConsumerRecord<String, SesEmailMessageDto> signDocumentsRecord,
                                      Acknowledgment acknowledgment) {
        executorServiceSendDocuments.submit(() -> {
            log.debug("consume message: offset={}, key={} from topic: send-ses",
                    signDocumentsRecord.offset(), signDocumentsRecord.key());
            dossierService.sendSesCode(signDocumentsRecord.value());
            acknowledgment.acknowledge();
        });
    }
}
