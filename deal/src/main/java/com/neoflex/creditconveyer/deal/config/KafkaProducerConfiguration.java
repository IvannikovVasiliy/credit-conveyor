package com.neoflex.creditconveyer.deal.config;

import com.neoflex.creditconveyer.deal.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.deal.domain.dto.InformationEmailMessage;
import com.neoflex.creditconveyer.deal.domain.dto.SesEmailMessage;
import com.neoflex.creditconveyer.deal.utils.KafkaProducerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

    @Value(value = "${spring.kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, EmailMessage> emailMessageProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, KafkaProducerConfig.MAX_BLOCK_MS_CONFIG);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
//
//    @Bean
//    public ProducerFactory<String, EmailMessage> emailMessageStringProducerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }

    @Bean
    public ProducerFactory<String, InformationEmailMessage> emailCreditMessageProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, KafkaProducerConfig.MAX_BLOCK_MS_CONFIG);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ProducerFactory<String, SesEmailMessage> emailSesCodeMessageProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, KafkaProducerConfig.MAX_BLOCK_MS_CONFIG);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, EmailMessage> emailKafkaTemplate() {
        return new KafkaTemplate<>(emailMessageProducerFactory());
    }

//    @Bean
//    public KafkaTemplate<String, EmailMessage> emailKafkaStringTemplate() {
//        return new KafkaTemplate<>(emailMessageStringProducerFactory());
//    }

    @Bean
    public KafkaTemplate<String, InformationEmailMessage> creditEmailKafkaTemplate() {
        return new KafkaTemplate<>(emailCreditMessageProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, SesEmailMessage> emailSesCodeKafkaTemplate() {
        return new KafkaTemplate<>(emailSesCodeMessageProducerFactory());
    }
}
