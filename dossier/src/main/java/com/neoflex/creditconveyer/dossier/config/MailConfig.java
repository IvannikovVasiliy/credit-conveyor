package com.neoflex.creditconveyer.dossier.config;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private Integer port;
    @Value("${mail.email}")
    private String email;
    @Value("${mail.password}")
    private String password;
    @Value("${sftp.host}")
    private String SFTP_HOST;
    @Value("${sftp.port}")
    private Integer SFTP_PORT;
    @Value("${sftp.user}")
    private String SFTP_USER;
    @Value("${sftp.password}")
    private String SFTP_PASSWORD;

    @Bean
    public JavaMailSender mailSender() throws IOException {
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
