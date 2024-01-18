package com.neoflex.creditconveyer.dossier.config;

import com.neoflex.creditconveyer.dossier.util.MailConfig;
import com.neoflex.creditconveyer.dossier.util.SecretConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private Integer port;
    @Value("${mail.email}")
    private String email;

    @Bean
    public JavaMailSender mailSender() throws IOException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(email);
        mailSender.setPassword(SecretConfig.getMailPasswordConfig());

        Properties properties = new Properties();
        properties.put(MailConfig.MAIL_TRANSPORT_PROTOCOL_CONFIG, "smtp");
        properties.put(MailConfig.MAIL_SMTP_AUTH_CONFIG, "true");
        properties.put(MailConfig.MAIL_SMTP_STARTTLS_ENABLE_CONFIG, "true");
        properties.put(MailConfig.MAIL_DEBUG_CONFIG, "true");

        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }
}
