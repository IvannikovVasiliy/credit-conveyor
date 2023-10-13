package com.neoflex.creditconveyer.dossier.service.impl;

import com.neoflex.creditconveyer.dossier.domain.constant.PaymentConstants;
import com.neoflex.creditconveyer.dossier.domain.dto.CreditEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.SesEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.entity.DocumentEntity;
import com.neoflex.creditconveyer.dossier.domain.model.CustomEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.model.DocumentModel;
import com.neoflex.creditconveyer.dossier.feign.DealFeignService;
import com.neoflex.creditconveyer.dossier.repository.DocumentRepository;
import com.neoflex.creditconveyer.dossier.service.DocumentService;
import com.neoflex.creditconveyer.dossier.service.DossierService;
import com.neoflex.creditconveyer.dossier.service.EmailSender;
import com.neoflex.creditconveyer.dossier.service.FileWorker;
import com.neoflex.creditconveyer.dossier.util.ConfigUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.OpenMode;
import net.schmizz.sshj.sftp.RemoteFile;
import net.schmizz.sshj.sftp.SFTPEngine;
import net.schmizz.sshj.sftp.StatefulSFTPClient;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.util.Map;
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
    @Value("${sftp.host}")
    private String sftpHost;
    @Value("${sftp.user}")
    private String sftpUser;
    @Value("${sftp.password}")
    private String sftpPassword;
    @Value("${sftp.sshHostsFileName")
    private String sshHostsFileName;

    private final EmailSender emailSender;
    private final FileWorker fileWorker;
    private final DealFeignService dealFeignService;
    private final DocumentRepository documentRepository;
    private final DocumentService documentService;

    @Override
    public void finishRegistration(EmailMessage emailMessage) {
        log.debug("Input finishRegistration. emailMessage={ address: {}, theme: {}, applicationId: {} }",
                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId());

        CustomEmailMessage customEmailMessage = new CustomEmailMessage(
                emailMessage.getAddress(),
                emailMessage.getTheme().name(),
                ConfigUtils.getTextFinishRegistration().replace("%applicationId%", emailMessage.getApplicationId().toString())
        );
        emailSender.sendMail(customEmailMessage);

        log.debug("Output finishRegistration for applicationId={}", emailMessage.getApplicationId());
    }

    @Override
    @Transactional
    public void createDocuments(CreditEmailMessage emailMessage) {
        log.debug("Input createDocuments. emailMessage={ address: {}, theme: {}, applicationId: {} }",
                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId());

        SSHClient sshClient = connectSshClient();
        StatefulSFTPClient statefulSFTPClient = createSftpClient(sshClient);

        DocumentModel loanAgreementDocument = documentService.createLoanAgreement(emailMessage.getApplicationId(), emailMessage.getCredit());
        fileWorker.writeFileInRemoteServer(statefulSFTPClient, loanAgreement, loanFileName);

        String questionnaire = ConfigUtils.getTextQuestionnaire().replace("%applicationId%", emailMessage.getApplicationId().toString());
        String questionnaireName = UUID.randomUUID() + " questionnaire " + emailMessage.getApplicationId();
        fileWorker.writeFileInRemoteServer(statefulSFTPClient, questionnaire, questionnaireName);

        StringBuilder paymentScheduleBuilder = new StringBuilder(ConfigUtils.getTextPaymentSchedule()).append("\n");
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
        fileWorker.writeFileInRemoteServer(statefulSFTPClient, paymentScheduleBuilder.toString(), paymentScheduleFileName);

        DocumentEntity documentEntity = new DocumentEntity(emailMessage.getApplicationId(), loanFileName, questionnaireName, paymentScheduleFileName);
        documentRepository.save(documentEntity);

        CustomEmailMessage customEmailMessage = new CustomEmailMessage(
                emailMessage.getAddress(),
                emailMessage.getTheme().name(),
                ConfigUtils.getTextCreateDocuments().replace("%applicationId%", emailMessage.getApplicationId().toString())
        );
        emailSender.sendMail(customEmailMessage);

        log.debug("Output createDocuments for applicationId={}", emailMessage.getApplicationId());
    }

    @Override
    @Transactional
    public void sendDocuments(EmailMessage emailMessage) {
        log.debug("Input sendDocuments. emailMessage={ address: {}, theme: {}, applicationId: {} }",
                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId());

        dealFeignService.updateStatus(emailMessage.getApplicationId());

        DocumentEntity documentEntity = documentRepository
                .findById(emailMessage.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Not found. Application with id=%s not found", emailMessage.getApplicationId())));

        try {
            SSHClient sshClient = connectSshClient();
            StatefulSFTPClient statefulSFTPClient = createSftpClient(sshClient);

            RemoteFile remoteLoanFile = statefulSFTPClient.open(locationFiles + "/" + documentEntity.getLoanAgreementName(), Set.of(OpenMode.READ));
            RemoteFile remoteQuestionnaireFile = statefulSFTPClient.open(locationFiles + "/" + documentEntity.getQuestionnaireName(), Set.of(OpenMode.READ));
            RemoteFile remotePaymentScheduleFile = statefulSFTPClient.open(locationFiles + "/" + documentEntity.getLoanAgreementName(), Set.of(OpenMode.READ));
            try (InputStream loanInputStream = remoteLoanFile.new RemoteFileInputStream();
                 InputStream questionnaireInputStream = remoteQuestionnaireFile.new RemoteFileInputStream();
                 InputStream paymentScheduleInputStream = remotePaymentScheduleFile.new RemoteFileInputStream()) {
                CustomEmailMessage customEmailMessage = new CustomEmailMessage(
                        emailMessage.getAddress(),
                        emailMessage.getTheme().name(),
                        ConfigUtils.getTextCreateDocuments().replace("%applicationId%", emailMessage.getApplicationId().toString())
                );
                Map<String, ByteArrayResource> files = Map.of(
                    "Loan agreement.txt", new ByteArrayResource(loanInputStream.readAllBytes()),
                    "Questionnaire.txt", new ByteArrayResource(questionnaireInputStream.readAllBytes()),
                    "Payment schedule.txt", new ByteArrayResource(paymentScheduleInputStream.readAllBytes())
                );
                emailSender.sendMimeMail(customEmailMessage, files);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.debug("Output sendDocuments for applicationId={}", emailMessage.getApplicationId());
    }

    @Override
    public void sendSesCode(SesEmailMessage sesEmailMessage) {
        log.debug("Input finishRegistration. sesEmailMessage={ address: {}, theme: {}, applicationId: {} }",
                sesEmailMessage.getAddress(), sesEmailMessage.getTheme(), sesEmailMessage.getApplicationId());

        CustomEmailMessage customEmailMessage = new CustomEmailMessage(
                sesEmailMessage.getAddress(),
                sesEmailMessage.getTheme().name(),
                ConfigUtils.getTextSesCode().replace("%sesCode%", sesEmailMessage.getSesCode().toString())
        );
        emailSender.sendMail(customEmailMessage);

        log.debug("Output finishRegistration for applicationId={}", sesEmailMessage.getApplicationId());
    }

    @Override
    public void sendIssuedCreditEmail(EmailMessage emailMessage) {
        log.debug("Input sendIssuedCreditEmail. emailMessage={ address: {}, theme: {}, applicationId: {} }",
                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId());

        CustomEmailMessage customEmailMessage = new CustomEmailMessage(
                emailMessage.getAddress(),
                emailMessage.getTheme().name(),
                ConfigUtils.getTextIssued().replace("%applicationId%", emailMessage.getApplicationId().toString())
        );
        emailSender.sendMail(customEmailMessage);

        log.debug("Output sendIssuedCreditEmail for applicationId={}", emailMessage.getApplicationId());
    }

    @Override
    public void sendApplicationDeniedEmail(EmailMessage emailMessage) {
        log.debug("Input sendApplicationDeniedEmail. emailMessage={ address: {}, theme: {}, applicationId: {} }",
                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId());

        CustomEmailMessage customEmailMessage = new CustomEmailMessage(
                emailMessage.getAddress(),
                emailMessage.getTheme().name(),
                ConfigUtils.getTextApplicationDenied().replace("%applicationId%", emailMessage.getApplicationId().toString())
        );
        emailSender.sendMail(customEmailMessage);

        log.debug("Output sendApplicationDeniedEmail for applicationId={}", emailMessage.getApplicationId());
    }

    private SSHClient connectSshClient() {
        log.debug("Input. connectSshClient");

        SSHClient sshClient = new SSHClient();
        try {
            sshClient.loadKnownHosts(new File(sshHostsFileName));
            sshClient.connect(sftpHost);
            sshClient.authPassword(sftpUser, sftpPassword);
        } catch (IOException e) {
            log.error("Output error. Exception loading hosts");
            throw new RuntimeException(e);
        }

        log.debug("Output. Success connect to SFTP-server");
        return sshClient;
    }

    private StatefulSFTPClient createSftpClient(SSHClient sshClient) {
        log.debug("Input createSftpClient by sshClient");

        StatefulSFTPClient statefulSFTPClient;
        try {
            SFTPEngine sftpEngine = new SFTPEngine(sshClient).init();
            statefulSFTPClient = new StatefulSFTPClient(sftpEngine);
            statefulSFTPClient.cd(locationFiles);
        } catch (IOException e) {
            log.error("Error in createSftpClient");
            throw new RuntimeException(e);
        }

        log.debug("Input createSftpClient. StatefulSFTPClient was created");
        return statefulSFTPClient;
    }
}
