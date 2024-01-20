package com.neoflex.creditconveyer.dossier.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DossierConsumer {

    @KafkaListener(topics = "${kafka.topics.create-documents-topic}", groupId = "${kafka.group-id}", containerFactory = "")
}
