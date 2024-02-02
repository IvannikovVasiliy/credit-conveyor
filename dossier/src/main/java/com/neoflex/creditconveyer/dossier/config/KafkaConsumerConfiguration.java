package com.neoflex.creditconveyer.dossier.config;

import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessageDto;
import com.neoflex.creditconveyer.dossier.domain.dto.InformationEmailMessageDto;
import com.neoflex.creditconveyer.dossier.domain.dto.SesEmailMessageDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfiguration {

    @Value("${kafka.bootstrap-server}")
    private String BOOTSTRAP_SERVER;
    @Value("${kafka.group-id}")
    private String DOSSIER_GROUP;

    @Bean
    public ConsumerFactory<String, EmailMessageDto> finishRegistartionConsumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, DOSSIER_GROUP);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(
                properties, new StringDeserializer(), new JsonDeserializer<>(EmailMessageDto.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailMessageDto> finishRegistrationKafkaListener() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, EmailMessageDto>();
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(finishRegistartionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, InformationEmailMessageDto> createDocumentConsumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, DOSSIER_GROUP);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(
                properties, new StringDeserializer(), new JsonDeserializer<>(InformationEmailMessageDto.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InformationEmailMessageDto> createDocumentKafkaListener() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, InformationEmailMessageDto>();
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(createDocumentConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, EmailMessageDto> sendDocumentsConsumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, DOSSIER_GROUP);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(
                properties, new StringDeserializer(), new JsonDeserializer<>(EmailMessageDto.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailMessageDto> sendDocumentsKafkaListener() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, EmailMessageDto>();
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(sendDocumentsConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, SesEmailMessageDto> signDocumentsConsumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, DOSSIER_GROUP);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(
                properties, new StringDeserializer(), new JsonDeserializer<>(SesEmailMessageDto.class, false)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SesEmailMessageDto> signDocumentsKafkaListener() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, SesEmailMessageDto>();
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(signDocumentsConsumerFactory());
        return factory;
    }
}
