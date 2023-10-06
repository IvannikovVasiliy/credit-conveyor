package com.neoflex.creditconveyer.dossier.service.impl;

import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.dossier.service.DossierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DossierServiceImpl implements DossierService {

    @Value("${email}")
    private String email;

    private final MailSender mailSender;

    @Override
    public void finishRegistration(EmailMessage emailMessage) {
        log.debug("Input finishRegistration. emailMessage={ address: {}, theme: {}, applicationId: {} }",
                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId());

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailMessage.getAddress());
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setSubject(emailMessage.getTheme().name());
        simpleMailMessage.setText("Finish registration application with id=" + emailMessage.getApplicationId());

        mailSender.send(simpleMailMessage);
    }
}
