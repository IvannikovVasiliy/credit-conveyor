package com.neoflex.creditconveyer.dossier.service.impl;

import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.InformationEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.SesEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.entity.DocumentEntity;
import com.neoflex.creditconveyer.dossier.domain.model.CustomEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.model.DocumentModel;
import com.neoflex.creditconveyer.dossier.factory.SFTPFactory;
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
import net.schmizz.sshj.sftp.StatefulSFTPClient;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DossierServiceImpl implements DossierService {

    @Value("${mail.email}")
    private String EMAIL_CONFIG;
    @Value("${sftp.locationFiles}")
    private String LOCATION_FILES_CONFIG;
    @Value("${sftp.host}")
    private String SFTP_HOST_CONFIG;
    @Value("${sftp.sshClientPool}")
    private Integer SSH_CLIENT_POOL;

    private final EmailSender emailSender;
    private final FileWorker fileWorker;
    private final DealFeignService dealFeignService;
    private final DocumentRepository documentRepository;
    private final DocumentService documentService;
    private final List<SSHClient> sshClients;
    private final SFTPFactory sftpFactory;

    private Random random = new Random();

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
    public void createDocuments(InformationEmailMessage emailMessage) {
        log.debug("Input createDocuments. emailMessage={ address: {}, theme: {}, applicationId: {} client={ lastName: {}, firstName: {}, middleName: {}, birthdate: {}, email: {},  martialStatus: {},  dependentAmount: {}, passport: {}, employment: {},  account: {} }; application: { status: {} creationDate: {},  appliedOffer: {},  statusHistory: {} }; credit: { amount: {}, term: {}, monthlyPayment: {}, rate: {}, psk: {}, paymentSchedule: {}, insuranceEnable: {}, salaryClient: {}, creditStatus: {} } }",
                emailMessage.getAddress(), emailMessage.getTheme(), emailMessage.getApplicationId(), emailMessage.getClient().getLastName(), emailMessage.getClient().getFirstName(), emailMessage.getClient().getMiddleName(), emailMessage.getClient().getBirthdate(), emailMessage.getClient().getEmail(), emailMessage.getClient().getMartialStatus(), emailMessage.getClient().getDependentAmount(), emailMessage.getClient().getPassport(), emailMessage.getClient().getEmployment(), emailMessage.getClient().getAccount(), emailMessage.getApplication().getStatus(), emailMessage.getApplication().getCreationDate(), emailMessage.getApplication().getAppliedOffer(), emailMessage.getApplication().getStatusHistory(), emailMessage.getCredit().getAmount(), emailMessage.getCredit().getTerm(), emailMessage.getCredit().getMonthlyPayment(), emailMessage.getCredit().getRate(), emailMessage.getCredit().getPsk(), emailMessage.getCredit().getPaymentSchedule(), emailMessage.getCredit().getInsuranceEnable(), emailMessage.getCredit().getSalaryClient(), emailMessage.getCredit().getCreditStatus());

//        SSHClient sshClient = connectSshClient();
        StatefulSFTPClient statefulSFTPClient = sftpFactory.getStatefulSFTPClient();

        DocumentModel loanAgreementDocument = documentService.createLoanAgreement(emailMessage.getApplicationId(), emailMessage.getClient(), emailMessage.getApplication(), emailMessage.getCredit());
        fileWorker.writeFileInRemoteServer(
                statefulSFTPClient, loanAgreementDocument.getFileName(), loanAgreementDocument.getFileText()
        );

        DocumentModel questionnaireDocument = documentService.createQuestionnaire(emailMessage.getApplicationId(), emailMessage.getClient(), emailMessage.getApplication().getAppliedOffer(), emailMessage.getCredit());
        fileWorker.writeFileInRemoteServer(
                statefulSFTPClient, questionnaireDocument.getFileName(), questionnaireDocument.getFileText()
        );

        DocumentModel paymentScheduleDocument = documentService.createPaymentSchedule(emailMessage.getApplicationId(), emailMessage.getCredit().getPaymentSchedule());
        fileWorker.writeFileInRemoteServer(
                statefulSFTPClient, paymentScheduleDocument.getFileName(), paymentScheduleDocument.getFileText()
        );

        DocumentEntity documentEntity = new DocumentEntity(
                emailMessage.getApplicationId(),
                loanAgreementDocument.getFileName(),
                loanAgreementDocument.getFileName(),
                loanAgreementDocument.getFileName()
        );
        documentRepository.save(documentEntity);

        CustomEmailMessage customEmailMessage = new CustomEmailMessage(
                emailMessage.getAddress(),
                emailMessage.getTheme().name(),
                ConfigUtils.getTextCreateDocuments().replace("%applicationId%", emailMessage.getApplicationId().toString())
        );
//         а что если сообщение не отправится?
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
//            SSHClient sshClient = connectSshClient();
            StatefulSFTPClient statefulSFTPClient = sftpFactory.getStatefulSFTPClient();

            RemoteFile remoteLoanFile = statefulSFTPClient.open(LOCATION_FILES_CONFIG + "/" + documentEntity.getLoanAgreementName(), Set.of(OpenMode.READ));
            RemoteFile remoteQuestionnaireFile = statefulSFTPClient.open(LOCATION_FILES_CONFIG + "/" + documentEntity.getQuestionnaireName(), Set.of(OpenMode.READ));
            RemoteFile remotePaymentScheduleFile = statefulSFTPClient.open(LOCATION_FILES_CONFIG + "/" + documentEntity.getLoanAgreementName(), Set.of(OpenMode.READ));
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

//    private SSHClient connectSshClient() {
//        log.debug("Input. connectSshClient");
//
//        SSHClient sshClient = new SSHClient();
//        sshClient.setConnectTimeout(1_000);
//        try {
//            sshClient.loadKnownHosts(new File(SSH_HOSTS_FILE_NAME_CONFIG));
//            sshClient.connect(SFTP_HOST_CONFIG);
//            sshClient.authPassword(SecretConfig.getSftpUserConfig(), SecretConfig.getSftpPasswordConfig());
//        } catch (IOException e) {
//            log.error("Output error. Exception loading hosts");
//            throw new RuntimeException(e);
//        }
//
//        log.debug("Output. Success connect to SFTP-server");
//        return sshClient;
//    }

//    private StatefulSFTPClient createSftpClient(SSHClient sshClient) {
//        log.debug("Input createSftpClient by sshClient");
//
//        StatefulSFTPClient statefulSFTPClient;
//        try {
//            SFTPEngine sftpEngine = new SFTPEngine(sshClient).init();
//            statefulSFTPClient = new StatefulSFTPClient(sftpEngine);
//            statefulSFTPClient.cd(LOCATION_FILES_CONFIG);
//        } catch (IOException e) {
//            log.error("Error in createSftpClient");
//            throw new RuntimeException(e);
//        }
//
//        log.debug("Input createSftpClient. StatefulSFTPClient was created");
//        return statefulSFTPClient;
//    }
}
