package com.neoflex.creditconveyer.deal.kafka.producer;

import com.neoflex.creditconveyer.deal.domain.dto.CreditEmailMessage;
import com.neoflex.creditconveyer.deal.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.deal.domain.dto.SesEmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailProducer {

    private final KafkaTemplate<Long, EmailMessage> emailKafkaTemplate;
    private final KafkaTemplate<Long, CreditEmailMessage> creditEmailKafkaTemplate;
    private final KafkaTemplate<Long, SesEmailMessage> sesEmailKafkaTemplate;

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

    public void sendSesCodeEmailMessage(final String TOPIC, SesEmailMessage emailMessage) {
        CompletableFuture<SendResult<Long, SesEmailMessage>> future =
                sesEmailKafkaTemplate.send(TOPIC, emailMessage.getApplicationId(), emailMessage);

        future.whenCompleteAsync((result, exception) -> {
            if (null != exception) {
                log.error("error. Unable to send message with key={} message={ address: {}, theme: {}, applicationId: {}, sesCode: {} } due to : {}",
                        emailMessage.getApplicationId(), emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId(), emailMessage.getSesCode(), exception.getMessage());
            } else {
                log.info("Sent message={ address: {}, theme: {}, applicationId: {}, sesCode: {} } with offset=={}",
                        emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId(), emailMessage.getSesCode(), result.getRecordMetadata().offset());
            }
        });
    }
}
