package com.neoflex.creditconveyer.deal.kafka.producer;

import com.neoflex.creditconveyer.deal.domain.dto.CreditEmailMessage;
import com.neoflex.creditconveyer.deal.domain.dto.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailProducer {

    private final KafkaTemplate<Long, EmailMessage> emailKafkaTemplate;
    private final KafkaTemplate<Long, CreditEmailMessage> creditEmailKafkaTemplate;

    public void sendEmailMessage(final String TOPIC, EmailMessage emailMessage) {
        CompletableFuture<SendResult<Long, EmailMessage>> future = emailKafkaTemplate.send(TOPIC, emailMessage.getApplicationId(), emailMessage);

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

    public void sendCreditEmailMessage(final String TOPIC, CreditEmailMessage emailMessage) {
        CompletableFuture<SendResult<Long, CreditEmailMessage>> future =
                creditEmailKafkaTemplate.send(TOPIC, emailMessage.getApplicationId(), emailMessage);

        future.whenCompleteAsync((result, exception) -> {
            if (null != exception) {
                log.error("error. Unable to send message with key={} message={ address: {}, theme: {}, applicationId: {}, credit: {} } due to : {}",
                        emailMessage.getApplicationId(), emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId(), emailMessage.getCredit(), exception.getMessage());
            } else {
                log.info("Sent message={ address: {}, theme: {}, applicationId: {}, credit: {} } with offset=={}",
                        emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId(), emailMessage.getCredit(), result.getRecordMetadata().offset());
            }
        });
    }
}
