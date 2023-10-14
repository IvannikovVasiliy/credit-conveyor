package com.neoflex.creditconveyer.dossier.config;

import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.InformationEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.SesEmailMessage;
import com.neoflex.creditconveyer.dossier.service.DossierService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class DossierConfig {

    private final DossierService dossierService;

    @Bean
    public Consumer<EmailMessage> consumerFinishRegistrationBinding() {
        return emailMessage ->
                dossierService.finishRegistration(emailMessage);
    }

    @Bean
    public Consumer<InformationEmailMessage> consumerCreateDocumentsBinding() {
        return emailMessage ->
                dossierService.createDocuments(emailMessage);
    }

    @Bean
    public Consumer<EmailMessage> consumerSendDocumentsBinding() {
        return emailMessage ->
                dossierService.sendDocuments(emailMessage);
    }

    @Bean
    public Consumer<SesEmailMessage> consumerSendSesCodeBinding() {
        return emailMessage ->
                dossierService.sendSesCode(emailMessage);
    }

    @Bean
    public Consumer<EmailMessage> consumerSendIssuedCreditBinding() {
        return emailMessage ->
                dossierService.sendIssuedCreditEmail(emailMessage);
    }

    @Bean
    public Consumer<EmailMessage> consumerApplicationDeniedBinding() {
        return emailMessage ->
                dossierService.sendApplicationDeniedEmail(emailMessage);
    }
}
