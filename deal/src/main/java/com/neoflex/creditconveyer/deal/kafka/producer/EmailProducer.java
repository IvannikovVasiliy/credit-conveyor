package com.neoflex.creditconveyer.deal.kafka.producer;

import com.neoflex.creditconveyer.deal.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.deal.domain.dto.InformationEmailMessage;
import com.neoflex.creditconveyer.deal.domain.dto.SesEmailMessage;
import com.neoflex.creditconveyer.deal.error.exception.KafkaMessageNotSentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailProducer {

    private final KafkaTemplate<String, EmailMessage> emailKafkaTemplate;
    private final KafkaTemplate<String, InformationEmailMessage> creditEmailKafkaTemplate;
    private final KafkaTemplate<String, SesEmailMessage> sesEmailKafkaTemplate;

    public void sendEmailMessage(final String TOPIC, String key, EmailMessage emailMessage) {
        emailKafkaTemplate.execute(producer -> {
            ProducerRecord<String, EmailMessage> producerRecord = new ProducerRecord<>(TOPIC, key, emailMessage);
            producer.send(
                    producerRecord,
                    (result, exception) -> {
                        if (null != exception) {
                            throw new KafkaMessageNotSentException(String.format("error. Unable to send message: key=%s, applicationId=%s, topic: %s. Error message: %s",
                                    key, emailMessage.getApplicationId(), TOPIC, exception.getMessage()));
                        } else {
                            log.info("Sent message={ address: {}, theme: {}, applicationId: {} } with offset=={}",
                                    emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId(), result.offset());
                        }
                    });
            return Void.class;
        });
    }

    public void sendCreditEmailMessage(final String TOPIC, String key, InformationEmailMessage emailMessage) {
        creditEmailKafkaTemplate.execute(producer -> {
            ProducerRecord<String, InformationEmailMessage> producerRecord = new ProducerRecord<>(TOPIC, key, emailMessage);
            producer.send(
                    producerRecord,
                    (result, exception) -> {
                        if (null != exception) {
                            log.error("error. Unable to send message with key={} message={ address: {}, theme: {}, applicationId: {}, credit: {} } due to : {}",
                                    emailMessage.getApplicationId(), emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId(), emailMessage.getCredit(), exception.getMessage());
                            throw new KafkaMessageNotSentException(String.format("Unable to send message with applicationId=%d", emailMessage.getApplicationId()));
                        } else {
                            log.info("Sent message={ address: {}, theme: {}, applicationId: {}, credit: {} } with offset=={}",
                                    emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId(), emailMessage.getCredit(), result.offset());
                        }
                    });
            return Void.class;
        });
    }

    public void sendSesCodeEmailMessage(final String TOPIC, String key, SesEmailMessage emailMessage) {
        sesEmailKafkaTemplate.execute(producer -> {
            ProducerRecord<String, SesEmailMessage> producerRecord = new ProducerRecord<>(TOPIC, key, emailMessage);
            producer.send(
                    producerRecord,
                    (result, exception) -> {
                        if (null != exception) {
                            log.error("error. Unable to send message with key={} message={ address: {}, theme: {}, applicationId: {}, sesCode: {} } due to : {}",
                                    emailMessage.getApplicationId(), emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId(), emailMessage.getSesCode(), exception.getMessage());
                        } else {
                            log.info("Sent message={ address: {}, theme: {}, applicationId: {}, sesCode: {} } with offset=={}",
                                    emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId(), emailMessage.getSesCode(), result.offset());
                        }
                    });
            return Void.class;
        });
    }
}
