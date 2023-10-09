package com.neoflex.creditconveyer.dossier.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.dossier.service.DossierService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class DossierConfig {

    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private Integer port;
    @Value("${mail.email}")
    private String email;
    @Value("${mail.password}")
    private String password;
    @Value("${sftp.host}")
    private final String SFTP_HOST;
    @Value("${sftp.port}")
    private final Integer SFTP_PORT;
    @Value("${sftp.user}")
    private final String SFTP_USER;
    @Value("${sftp.password}")
    private final String SFTP_PASSWORD;

    private final DossierService dossierService;

    @Bean
    public MailSender mailSender() throws IOException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(email);
        mailSender.setPassword(password);

        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }

    @Bean
    public Consumer<EmailMessage> consumerFinishRegistrationBinding() {
        return emailMessage ->
                dossierService.finishRegistration(emailMessage);
    }

    @Bean
    public Consumer<EmailMessage> consumerCreateDocumentsBinding() {
        return emailMessage ->
                dossierService.createDocuments(emailMessage);
    }

    @Bean
    public Session getSession() {
        JSch jSch = new JSch();
        Session session = null;
        try {
            session = jSch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
            session.setPassword(SFTP_PASSWORD);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
        return session;
    }

    @PreDestroy
    public void destroy() {
        Session session = getSession();
        session.disconnect();
    }
}
