package com.neoflex.creditconveyer.dossier.service.impl;

import com.neoflex.creditconveyer.dossier.domain.model.CustomEmailMessage;
import com.neoflex.creditconveyer.dossier.service.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderImpl implements EmailSender {

    @Value("${mail.email}")
    private String email;

    private final JavaMailSender mailSender;

    @Override
    public void sendMail(CustomEmailMessage emailMessage) {
        log.debug("Input sendMail. emailAddress={}, theme={}, text={}",
                emailMessage.getEmail(), emailMessage.getTheme(), emailMessage.getText());

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailMessage.getEmail());
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setSubject(emailMessage.getTheme());
        simpleMailMessage.setText(emailMessage.getText());
        mailSender.send(simpleMailMessage);

        log.debug("Output sendMail. Mail was sent. emailAddress={}, theme={}, text={}",
                emailMessage.getEmail(), emailMessage.getTheme(), emailMessage.getText());
    }

    @Override
    public void sendMimeMail(CustomEmailMessage emailMessage, Map<String, ByteArrayResource> files) {
        log.debug("Input sendMimeMail. emailAddress={}, theme={}, text={}, files={}",
                emailMessage.getEmail(), emailMessage.getTheme(), emailMessage.getText(), files);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailMessage.getEmail());
            helper.setFrom(email);
            helper.setSubject(emailMessage.getTheme());
            helper.setText(emailMessage.getText());
            for (Map.Entry<String, ByteArrayResource> file : files.entrySet()) {
                helper.addAttachment(file.getKey(), file.getValue());
            }
            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Error sendMimeMail. Exception MimeMessageHelper");
            throw new RuntimeException(e);
        }

        log.debug("Output sendMimeMail. Mail was sent. emailAddress={}, theme={}, text={}, files={}",
                emailMessage.getEmail(), emailMessage.getTheme(), emailMessage.getText(), files);
    }
}
