package com.neoflex.creditconveyer.dossier.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dossier")
public class DossierController {

//    @Value("${email}")
//    private String email;

//    private final MailSender mailSender;

    @PostMapping
    public ResponseEntity<String> post() {
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setTo("ivannikovvasiliya2001@gmail.com");
//        simpleMailMessage.setFrom(email);
//        simpleMailMessage.setSubject("Проверочный код для регистрации");
//        int code = new Random().nextInt(90_000_000) + 10_000_000;
//        simpleMailMessage.setText("Проверочный код: " + code);
//
//        mailSender.send(simpleMailMessage);

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
