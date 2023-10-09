package com.neoflex.creditconveyer.dossier.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.neoflex.creditconveyer.dossier.domain.dto.CreditEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.dossier.service.DossierService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.util.Properties;
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
    public Consumer<EmailMessage> consumerSendDocumentsBinding() {
        return emailMessage ->
                dossierService.sendDocuments(emailMessage);
    }
}
