package com.neoflex.creditconveyer.dossier.service.impl;

import com.jcraft.jsch.*;
import com.neoflex.creditconveyer.dossier.domain.dto.CreditEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.dossier.domain.entity.DocumentEntity;
import com.neoflex.creditconveyer.dossier.feign.DealFeignService;
import com.neoflex.creditconveyer.dossier.repository.DocumentRepository;
import com.neoflex.creditconveyer.dossier.service.DossierService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DossierServiceImpl implements DossierService {

    @Value("${mail.email}")
    private String email;
    @Value("${sftp.locationFiles}")
    private String locationFiles;
    @Value("${application.finishRegistration}")
    private String textFinishRegistration;
    @Value("${application.createDocuments}")
    private String createDocuments;
    @Value("${application.loanAgreementText}")
    private String loanAgreementText;
    @Value("${application.questionnaireText}")
    private String questionnaireText;
    @Value("${application.paymentScheduleText}")
    private String paymentScheduleText;

    private final DocumentRepository documentrepository;
    private final JavaMailSender mailSender;
    private final Session session;
    private final DealFeignService dealFeignService;

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

        log.debug("Output finishRegistration for applicationId={}", emailMessage.getApplicationId());
    }

    @Override
    @Transactional
    public void createDocuments(CreditEmailMessage emailMessage) {
        log.debug("Input createDocuments. emailMessage={ address: {}, theme: {}, applicationId: {} }",
                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId());

        try {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            channelSftp.cd(locationFiles);

            String loanAgreement = loanAgreementText.replace("%applicationId%", emailMessage.getApplicationId().toString());
            ByteArrayInputStream loanAgreementInputStream = new ByteArrayInputStream(loanAgreement.getBytes());
            String loanAgreementName = UUID.randomUUID().toString()+" loan agreement "+emailMessage.getApplicationId();
            channelSftp.put(loanAgreementInputStream, loanAgreementName);

            String questionnaire = questionnaireText.replace("%applicationId%", emailMessage.getApplicationId().toString());
            ByteArrayInputStream questionnaireInputStream = new ByteArrayInputStream(questionnaire.getBytes());
            String questionnaireName = UUID.randomUUID()+" questionnaire "+emailMessage.getApplicationId();
            channelSftp.put(questionnaireInputStream, questionnaireName);

            StringBuilder paymentScheduleBuilder = new StringBuilder(paymentScheduleText);
            emailMessage
                    .getCredit()
                    .getPaymentSchedule()
                    .forEach(paymentScheduleElement -> paymentScheduleBuilder.append(paymentScheduleElement));
            ByteArrayInputStream paymentScheduleInputStream = new ByteArrayInputStream(paymentScheduleBuilder.toString().getBytes());
            String paymentScheduleName = UUID.randomUUID()+" payment schedule "+emailMessage.getApplicationId();
            channelSftp.put(paymentScheduleInputStream, paymentScheduleName);

            DocumentEntity documentEntity = new DocumentEntity(emailMessage.getApplicationId(), loanAgreementName, questionnaireName, paymentScheduleName);
            documentrepository.save(documentEntity);

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(emailMessage.getAddress());
            simpleMailMessage.setFrom(email);
            simpleMailMessage.setSubject(emailMessage.getTheme().name());
            simpleMailMessage.setText(createDocuments.replace("%applicationId%", emailMessage.getApplicationId().toString()));
            mailSender.send(simpleMailMessage);
        } catch (JSchException e) {
            log.error("Error send documents for applicationId={}", emailMessage.getApplicationId());
            throw new RuntimeException(e);
        } catch (SftpException e) {
            log.error("Error send documents for applicationId={}", emailMessage.getApplicationId());
            throw new RuntimeException(e);
        }

        log.debug("Output createDocuments for applicationId={}", emailMessage.getApplicationId());
    }

    @Override
    @Transactional
    public void sendDocuments(EmailMessage emailMessage) {
        log.debug("Input sendDocuments. emailMessage={ address: {}, theme: {}, applicationId: {} }",
                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId());

        dealFeignService.updateStatus(emailMessage.getApplicationId());

        DocumentEntity documentEntity = documentrepository
                .findById(emailMessage.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Not found. Application with id=%s not found", emailMessage.getApplicationId())));

        try {
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            channelSftp.cd(locationFiles);

            InputStream inputStream = channelSftp.get(documentEntity.getLoanAgreementName());

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailMessage.getAddress());
            helper.setFrom(email);
            helper.setSubject(emailMessage.getTheme().name());
            String text = createDocuments.replace("%applicationId%", emailMessage.getApplicationId().toString());
            helper.setText(text);
            helper.addAttachment("MyTestFile.txt", new ByteArrayResource(inputStream.readAllBytes()));

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }  catch (JSchException e) {
            throw new RuntimeException(e);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.debug("Output sendDocuments for applicationId={}", emailMessage.getApplicationId());
    }
}
