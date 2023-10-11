package com.neoflex.creditconveyer.dossier.service.impl;

import com.jcraft.jsch.*;
import com.neoflex.creditconveyer.dossier.domain.constant.PaymentConstants;
import com.neoflex.creditconveyer.dossier.domain.dto.CreditEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.SesEmailMessage;
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
import java.math.RoundingMode;
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
    private String textCreateDocuments;
    @Value("${application.loanAgreementText}")
    private String loanAgreementText;
    @Value("${application.questionnaireText}")
    private String questionnaireText;
    @Value("${application.paymentScheduleText}")
    private String paymentScheduleText;
    @Value("${application.sesCodeText}")
    private String sesCodeEmailText;
    @Value("${application.issuedText}")
    private String sesIssuedCreditText;
    @Value("${application.applicationDenied}")
    private String applicationDeniedText;

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

            StringBuilder paymentScheduleBuilder = new StringBuilder(paymentScheduleText).append("\n");
            emailMessage
                    .getCredit()
                    .getPaymentSchedule()
                    .forEach(paymentScheduleElement -> paymentScheduleBuilder
                            .append(String.format("Номер платежа: %d; ", paymentScheduleElement.getNumber()))
                            .append(String.format("Дата платежа: %s; ", paymentScheduleElement.getDate()))
                            .append(String.format("Сумма: %s; ", paymentScheduleElement.getTotalPayment().setScale(PaymentConstants.CLIENT_MONEY_ACCURACY, RoundingMode.DOWN)))
                            .append(String.format("Погашение основного долга: %s; ", paymentScheduleElement.getInterestPayment().setScale(PaymentConstants.CLIENT_MONEY_ACCURACY, RoundingMode.DOWN)))
                            .append(String.format("Выплата процентов: %s; ", paymentScheduleElement.getDebtPayment().setScale(PaymentConstants.CLIENT_MONEY_ACCURACY, RoundingMode.DOWN)))
                            .append(String.format("Остаток: %s ", paymentScheduleElement.getDebtPayment().setScale(PaymentConstants.CLIENT_MONEY_ACCURACY, RoundingMode.DOWN)))
                            .append("\n"));
            ByteArrayInputStream paymentScheduleInputStream = new ByteArrayInputStream(paymentScheduleBuilder.toString().getBytes());
            String paymentScheduleName = UUID.randomUUID()+" payment schedule "+emailMessage.getApplicationId();
            channelSftp.put(paymentScheduleInputStream, paymentScheduleName);

            DocumentEntity documentEntity = new DocumentEntity(emailMessage.getApplicationId(), loanAgreementName, questionnaireName, paymentScheduleName);
            documentrepository.save(documentEntity);

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(emailMessage.getAddress());
            simpleMailMessage.setFrom(email);
            simpleMailMessage.setSubject(emailMessage.getTheme().name());
            simpleMailMessage.setText(textCreateDocuments.replace("%applicationId%", emailMessage.getApplicationId().toString()));
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

            InputStream loanAgreementStream = channelSftp.get(documentEntity.getLoanAgreementName());
            InputStream questionnaireStream = channelSftp.get(documentEntity.getQuestionnaireName());
            InputStream paymentScheduleStream = channelSftp.get(documentEntity.getPaymentScheduleName());

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailMessage.getAddress());
            helper.setFrom(email);
            helper.setSubject(emailMessage.getTheme().name());
            String text = textCreateDocuments.replace("%applicationId%", emailMessage.getApplicationId().toString());
            helper.setText(text);
            helper.addAttachment("Loan agreement.txt", new ByteArrayResource(loanAgreementStream.readAllBytes()));
            helper.addAttachment("Questionnaire.txt", new ByteArrayResource(questionnaireStream.readAllBytes()));
            helper.addAttachment("Payment schedule.txt", new ByteArrayResource(paymentScheduleStream.readAllBytes()));

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

    @Override
    public void sendSesCode(SesEmailMessage sesEmailMessage) {
        log.debug("Input finishRegistration. sesEmailMessage={ address: {}, theme: {}, applicationId: {} }",
                sesEmailMessage.getAddress(), sesEmailMessage.getTheme(), sesEmailMessage.getApplicationId());

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(sesEmailMessage.getAddress());
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setSubject(sesEmailMessage.getTheme().name());
        simpleMailMessage.setText(sesCodeEmailText.replace("%sesCode%", sesEmailMessage.getSesCode().toString()));

        mailSender.send(simpleMailMessage);

        log.debug("Output finishRegistration for applicationId={}", sesEmailMessage.getApplicationId());
    }

    @Override
    public void sendIssuedCreditEmail(EmailMessage emailMessage) {
        log.debug("Input sendIssuedCreditEmail. emailMessage={ address: {}, theme: {}, applicationId: {} }",
                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId());

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailMessage.getAddress());
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setSubject(emailMessage.getTheme().name());
        simpleMailMessage.setText(sesIssuedCreditText.replace("%applicationId%", emailMessage.getApplicationId().toString()));

        mailSender.send(simpleMailMessage);

        log.debug("Output sendIssuedCreditEmail for applicationId={}", emailMessage.getApplicationId());
    }

    @Override
    public void sendApplicationDeniedEmail(EmailMessage emailMessage) {
        log.debug("Input sendIssuedCreditEmail. emailMessage={ address: {}, theme: {}, applicationId: {} }",
                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId());

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailMessage.getAddress());
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setSubject(emailMessage.getTheme().name());
        simpleMailMessage.setText(applicationDeniedText.replace("%applicationId%", emailMessage.getApplicationId().toString()));

        mailSender.send(simpleMailMessage);

        log.debug("Output sendIssuedCreditEmail for applicationId={}", emailMessage.getApplicationId());
    }
}
