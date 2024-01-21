package com.neoflex.creditconveyer.dossier.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neoflex.creditconveyer.dossier.domain.dto.InformationEmailMessage;
import org.apache.commons.collections4.functors.ConstantFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.*;

@Configuration
public class KafkaConsumerConfiguration {

    @Value("${kafka.bootstrap-server}")
    private String BOOTSTRAP_SERVER;
    @Value("${kafka.group-id}")
    private String DOSSIER_GROUP;

    @Bean
    public ConsumerFactory<String, InformationEmailMessage> createDocumentConsumerFactory() {
//        Properties properties = new Properties();
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, DOSSIER_GROUP);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(
                properties, new StringDeserializer(), new JsonDeserializer<>(InformationEmailMessage.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InformationEmailMessage> createDocumentKafkaListener() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, InformationEmailMessage>();
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(createDocumentConsumerFactory());
        return factory;
    }

}
