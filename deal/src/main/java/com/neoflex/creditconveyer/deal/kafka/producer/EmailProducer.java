package com.neoflex.creditconveyer.deal.kafka.producer;

import com.neoflex.creditconveyer.deal.domain.dto.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailProducer {

    @Autowired
    public EmailProducer(KafkaTemplate<Long, EmailMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private KafkaTemplate<Long, EmailMessage> kafkaTemplate;

    public void sendMessage(final String TOPIC, EmailMessage emailMessage) {
        CompletableFuture<SendResult<Long, EmailMessage>> future = kafkaTemplate.send(TOPIC, emailMessage.getApplicationId(), emailMessage);

        future.whenCompleteAsync((result, exception) -> {
            if (null != exception) {
                log.error("error. Unable to send message with key={} message={ address: {}, theme: {}, applicationId: {} } due to : {}",
                        emailMessage.getApplicationId(), emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId(), exception.getMessage());
            } else {
                log.info("Sent message={ address: {}, theme: {}, applicationId: {} } with offset=={}",
                        emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId(),  result.getRecordMetadata().offset());
            }
        });
    }
}
