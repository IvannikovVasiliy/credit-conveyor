package com.neoflex.creditconveyer.dossier.service.impl;

import com.jcraft.jsch.*;
import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.dossier.service.DossierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DossierServiceImpl implements DossierService {

    @Value("${email}")
    private String email;
    @Value("${sftp.locationFiles}")
    private String locationFiles;
    @Value("${application.finishRegistration}")
    private String textFinishRegistration;

    private final MailSender mailSender;
    private final Session session;

    @Override
    public void finishRegistration(EmailMessage emailMessage) {
        log.debug("Input finishRegistration. emailMessage={ address: {}, theme: {}, applicationId: {} }",
                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId());

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailMessage.getAddress());
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setSubject(emailMessage.getTheme().name());
        simpleMailMessage.setText(textFinishRegistration.replace("%applicationId%", emailMessage.getApplicationId().toString()));

        mailSender.send(simpleMailMessage);
    }

    @Override
    public void createDocuments(EmailMessage emailMessage) {
        log.debug("Input createDocuments. emailMessage={ address: {}, theme: {}, applicationId: {} }",
                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId());

        try {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            channelSftp.cd(locationFiles);

            String text =
            ByteArrayInputStream bytesInputStream = new ByteArrayInputStream();
            channelSftp.put()
        } catch (JSchException e) {
            throw new RuntimeException(e);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }
}
