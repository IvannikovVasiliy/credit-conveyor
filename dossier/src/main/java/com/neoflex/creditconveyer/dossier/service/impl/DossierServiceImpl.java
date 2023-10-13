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
import com.neoflex.creditconveyer.dossier.util.ConfigUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.SSHException;
import net.schmizz.sshj.sftp.*;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.util.Set;
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
    @Value("${sftp.host}")
    private String sftpHost;
    @Value("${sftp.user}")
    private String sftpUser;
    @Value("${sftp.password}")
    private String sftpPassword;

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
            SSHClient sshClient = connectSshClient();
            SFTPEngine sftpEngine = new SFTPEngine(sshClient).init();
            StatefulSFTPClient statefulSFTPClient = new StatefulSFTPClient(sftpEngine);

            statefulSFTPClient.cd(locationFiles);

            String loanAgreement = loanAgreementText.replace("%applicationId%", emailMessage.getApplicationId().toString());
            String loanFileName = UUID.randomUUID() + " loan agreement " + emailMessage.getApplicationId();
            writeFileInRemoteServer(statefulSFTPClient, loanAgreement, loanFileName);

            String questionnaire = questionnaireText.replace("%applicationId%", emailMessage.getApplicationId().toString());
            String questionnaireName = UUID.randomUUID() + " questionnaire " + emailMessage.getApplicationId();
            writeFileInRemoteServer(statefulSFTPClient, questionnaire, questionnaireName);

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
            String paymentScheduleFileName = UUID.randomUUID() + " payment schedule " + emailMessage.getApplicationId();
            writeFileInRemoteServer(statefulSFTPClient, paymentScheduleBuilder.toString(), paymentScheduleFileName);

            DocumentEntity documentEntity = new DocumentEntity(emailMessage.getApplicationId(), loanFileName, questionnaireName, paymentScheduleFileName);
            documentrepository.save(documentEntity);

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(emailMessage.getAddress());
            simpleMailMessage.setFrom(email);
            simpleMailMessage.setSubject(emailMessage.getTheme().name());
            simpleMailMessage.setText(textCreateDocuments.replace("%applicationId%", emailMessage.getApplicationId().toString()));
            mailSender.send(simpleMailMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.debug("Output createDocuments for applicationId={}", emailMessage.getApplicationId());
    }

//    @Override
//    @Transactional
//    public void createDocuments(CreditEmailMessage emailMessage) {
//        log.debug("Input createDocuments. emailMessage={ address: {}, theme: {}, applicationId: {} }",
//                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId());
//
//        try {
//            Channel channel = session.openChannel("sftp");
//            channel.connect();
//            ChannelSftp channelSftp = (ChannelSftp) channel;
//            channelSftp.cd(locationFiles);
//
//            String loanAgreement = loanAgreementText.replace("%applicationId%", emailMessage.getApplicationId().toString());
//            ByteArrayInputStream loanAgreementInputStream = new ByteArrayInputStream(loanAgreement.getBytes());
//            String loanAgreementName = UUID.randomUUID().toString()+" loan agreement "+emailMessage.getApplicationId();
//            channelSftp.put(loanAgreementInputStream, loanAgreementName);
//
//            String questionnaire = questionnaireText.replace("%applicationId%", emailMessage.getApplicationId().toString());
//            ByteArrayInputStream questionnaireInputStream = new ByteArrayInputStream(questionnaire.getBytes());
//            String questionnaireName = UUID.randomUUID()+" questionnaire "+emailMessage.getApplicationId();
//            channelSftp.put(questionnaireInputStream, questionnaireName);
//
//            StringBuilder paymentScheduleBuilder = new StringBuilder(paymentScheduleText).append("\n");
//            emailMessage
//                    .getCredit()
//                    .getPaymentSchedule()
//                    .forEach(paymentScheduleElement -> paymentScheduleBuilder
//                            .append(String.format("Номер платежа: %d; ", paymentScheduleElement.getNumber()))
//                            .append(String.format("Дата платежа: %s; ", paymentScheduleElement.getDate()))
//                            .append(String.format("Сумма: %s; ", paymentScheduleElement.getTotalPayment().setScale(PaymentConstants.CLIENT_MONEY_ACCURACY, RoundingMode.DOWN)))
//                            .append(String.format("Погашение основного долга: %s; ", paymentScheduleElement.getInterestPayment().setScale(PaymentConstants.CLIENT_MONEY_ACCURACY, RoundingMode.DOWN)))
//                            .append(String.format("Выплата процентов: %s; ", paymentScheduleElement.getDebtPayment().setScale(PaymentConstants.CLIENT_MONEY_ACCURACY, RoundingMode.DOWN)))
//                            .append(String.format("Остаток: %s ", paymentScheduleElement.getDebtPayment().setScale(PaymentConstants.CLIENT_MONEY_ACCURACY, RoundingMode.DOWN)))
//                            .append("\n"));
//            ByteArrayInputStream paymentScheduleInputStream = new ByteArrayInputStream(paymentScheduleBuilder.toString().getBytes());
//            String paymentScheduleName = UUID.randomUUID()+" payment schedule "+emailMessage.getApplicationId();
//            channelSftp.put(paymentScheduleInputStream, paymentScheduleName);
//
//            DocumentEntity documentEntity = new DocumentEntity(emailMessage.getApplicationId(), loanAgreementName, questionnaireName, paymentScheduleName);
//            documentrepository.save(documentEntity);
//
//            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//            simpleMailMessage.setTo(emailMessage.getAddress());
//            simpleMailMessage.setFrom(email);
//            simpleMailMessage.setSubject(emailMessage.getTheme().name());
//            simpleMailMessage.setText(textCreateDocuments.replace("%applicationId%", emailMessage.getApplicationId().toString()));
//            mailSender.send(simpleMailMessage);
//        } catch (JSchException e) {
//            log.error("Error send documents for applicationId={}", emailMessage.getApplicationId());
//            throw new RuntimeException(e);
//        } catch (SftpException e) {
//            log.error("Error send documents for applicationId={}", emailMessage.getApplicationId());
//            throw new RuntimeException(e);
//        }
//
//        log.debug("Output createDocuments for applicationId={}", emailMessage.getApplicationId());
//    }

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
        } catch (JSchException e) {
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
        log.debug("Input sendApplicationDeniedEmail. emailMessage={ address: {}, theme: {}, applicationId: {} }",
                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId());

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailMessage.getAddress());
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setSubject(emailMessage.getTheme().name());
        simpleMailMessage.setText(applicationDeniedText.replace("%applicationId%", emailMessage.getApplicationId().toString()));

        mailSender.send(simpleMailMessage);

        log.debug("Output sendApplicationDeniedEmail for applicationId={}", emailMessage.getApplicationId());
    }

    private SSHClient connectSshClient() {
        log.debug("Input. connectSshClient");

        SSHClient sshClient = new SSHClient();
        try {
            String hostsFileName = ConfigUtil.getHostsFileName();
            sshClient.loadKnownHosts(new File(hostsFileName));
            sshClient.connect(sftpHost);
            sshClient.authPassword(sftpUser, sftpPassword);
        } catch (IOException e) {
            log.error("Output error. Exception loading hosts");
            throw new RuntimeException(e);
        }

        log.debug("Output. Success connect to SFTP-server");
        return sshClient;
    }

    private void writeFileInRemoteServer(StatefulSFTPClient statefulSFTPClient, String text, String fileName) {
        log.debug("Input writeFileInRemoteServer");

        ByteArrayInputStream loanInputStream = new ByteArrayInputStream(text.getBytes());
        RemoteFile remoteFile = null;
        try {
            remoteFile = statefulSFTPClient.open(fileName, Set.of(OpenMode.CREAT, OpenMode.WRITE));
        } catch (IOException ex) {
            log.error("Error writeFileInRemoteServer. Exception in process opening file {}", fileName);
            throw new RuntimeException(ex);
        }

        try (RemoteFile.RemoteFileOutputStream outputStream = remoteFile.new RemoteFileOutputStream()) {
            byte[] loanAgreementTextInBytes = loanInputStream.readAllBytes();
            outputStream.write(loanAgreementTextInBytes);
        } catch (IOException ex) {
            log.error("Error writeFileInRemoteServer. Exception in process writing file {}", fileName);
            throw new RuntimeException(ex);
        }

        log.debug("Output writeFileInRemoteServer. File with fileName={} was successfully written", fileName);
    }
}
