package com.neoflex.creditconveyer.dossier.config;

import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.InformationEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.SesEmailMessage;
import com.neoflex.creditconveyer.dossier.service.DossierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Configuration
//@RequiredArgsConstructor
public class DossierConfiguration {

//    private final DossierService dossierService;

    @Value("${kafka.topics.create-documents-topic.count-consumers}")
    private Integer COUNT_CONSUMERS_CREATE_DOCUMENTS;

    @Bean
    public ExecutorService createDocumentsKafkaConsumerExecutorService() {
        return Executors.newFixedThreadPool(COUNT_CONSUMERS_CREATE_DOCUMENTS);
    }

//    @Bean
//    public Consumer<EmailMessage> consumerFinishRegistrationBinding() {
//        return emailMessage ->
//                dossierService.finishRegistration(emailMessage);
//    }
//
//    @Bean
//    public Consumer<InformationEmailMessage> consumerCreateDocumentsBinding() {
//        return emailMessage ->
//            dossierService.createDocuments(emailMessage);
//    }
//
//    @Bean
//    public Consumer<EmailMessage> consumerSendDocumentsBinding() {
//        return emailMessage ->
//                dossierService.sendDocuments(emailMessage);
//    }
//
//    @Bean
//    public Consumer<SesEmailMessage> consumerSendSesCodeBinding() {
//        return emailMessage ->
//                dossierService.sendSesCode(emailMessage);
//    }
//
//    @Bean
//    public Consumer<EmailMessage> consumerSendIssuedCreditBinding() {
//        return emailMessage ->
//                dossierService.sendIssuedCreditEmail(emailMessage);
//    }
//
//    @Bean
//    public Consumer<EmailMessage> consumerApplicationDeniedBinding() {
//        return emailMessage ->
//                dossierService.sendApplicationDeniedEmail(emailMessage);
//    }
}
