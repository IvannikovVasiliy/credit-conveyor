package com.neoflex.creditconveyer.dossier.kafka;

import com.neoflex.creditconveyer.dossier.domain.dto.InformationEmailMessage;
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
            DossierService dossierService
    ) {
        this.executorServiceCreateDocuments = executorServiceCreateDocuments;
        this.dossierService = dossierService;
    }

    private final ExecutorService executorServiceCreateDocuments;
    private final DossierService dossierService;

    @KafkaListener(
            topics = "${kafka.topics.create-documents-topic.name}",
            groupId = "${kafka.group-id}",
            containerFactory = "createDocumentKafkaListener"
    )
    public void createDocumentConsumer(ConsumerRecord<String, InformationEmailMessage> createDocumentRecord, Acknowledgment acknowledgment) {
        executorServiceCreateDocuments.submit(() -> {
            log.debug("consume message: offset={}, key={}", createDocumentRecord.offset(), createDocumentRecord.key());
            dossierService.createDocuments(createDocumentRecord.value(), acknowledgment);
//            acknowledgment.acknowledge();
        });
    }
}
