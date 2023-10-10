package com.neoflex.creditconveyer.dossier.config;

import com.neoflex.creditconveyer.dossier.domain.dto.CreditEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.EM2;
import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessage;
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
    public Consumer<CreditEmailMessage> consumerCreateDocumentsBinding() {
        return emailMessage ->
                dossierService.createDocuments(emailMessage);
    }

    @Bean
    public Consumer<EM2> consumerSendDocumentsBinding() {
        return emailMessage ->
                dossierService.sendDocuments(emailMessage);
    }
}
