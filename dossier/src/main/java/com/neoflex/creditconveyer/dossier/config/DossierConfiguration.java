package com.neoflex.creditconveyer.dossier.config;

import com.neoflex.creditconveyer.dossier.util.SecretConfig;
import com.neoflex.creditconveyer.dossier.util.SFTPConfig;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPEngine;
import net.schmizz.sshj.sftp.StatefulSFTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@Slf4j
//@RequiredArgsConstructor
public class DossierConfiguration {

//    private final DossierService dossierService;

    @Value("${kafka.topics.create-documents-topic.count-consumers}")
    private Integer COUNT_CONSUMERS_CREATE_DOCUMENTS;
    @Value("${kafka.topics.send-documents-topic.count-consumers}")
    private Integer COUNT_CONSUMERS_SEND_DOCUMENTS;
    @Value("${sftp.connectionTimeout}")
    private Integer CONNECTION_TIMEOUT_SFTP;
    @Value("${sftp.host}")
    private String SFTP_HOST_CONFIG;
    @Value("${sftp.sshClientPool}")
    private Integer SSH_CLIENT_POOL;
    @Value("${sftp.statefulSftpClientPool}")
    private Integer STATEFUL_SFTP_CLIENT_POOL;
    @Value("${sftp.locationFiles}")
    private String LOCATION_FILES_CONFIG;

    private AtomicInteger counter = new AtomicInteger();

    @Bean
    public ExecutorService createDocumentsKafkaConsumerExecutorService() {
        return Executors.newFixedThreadPool(COUNT_CONSUMERS_CREATE_DOCUMENTS);
    }

    @Bean
    public ExecutorService sendDocumentsKafkaConsumerExecutorService() {
        return Executors.newFixedThreadPool(COUNT_CONSUMERS_SEND_DOCUMENTS);
    }

    @Bean
    public List<SSHClient> sshClientPool() {
        List<SSHClient> sshClientPool = new ArrayList<>(SSH_CLIENT_POOL);
        for (int i = 0; i < SSH_CLIENT_POOL; i++) {
            SSHClient sshClient = new SSHClient();
            sshClient.setConnectTimeout(CONNECTION_TIMEOUT_SFTP);
            try {
                sshClient.loadKnownHosts(new File(SFTPConfig.getHostsFile()));
                sshClient.connect(SFTP_HOST_CONFIG);
                sshClient.authPassword(SecretConfig.getSftpUserConfig(), SecretConfig.getSftpPasswordConfig());
            } catch (IOException e) {
                log.error("Output error. Exception loading hosts");
                throw new RuntimeException(e);
            }
            sshClientPool.add(sshClient);
        }

        log.info("Created pool of SSHClient size of {}", SSH_CLIENT_POOL);
        return sshClientPool;
    }

    @Bean
    public List<StatefulSFTPClient> statefulSFTPClientPool(List<SSHClient> sshClientPool) {
        List<StatefulSFTPClient> statefulSFTPClients = new ArrayList<>(STATEFUL_SFTP_CLIENT_POOL);
        for (int i = 0; i < STATEFUL_SFTP_CLIENT_POOL; i++) {
            int indexSshClient = i % SSH_CLIENT_POOL;
            SSHClient sshClient = sshClientPool.get(indexSshClient);
            try {
                SFTPEngine sftpEngine = new SFTPEngine(sshClient).init();
                StatefulSFTPClient statefulSFTPClient = new StatefulSFTPClient(sftpEngine);
                statefulSFTPClient.cd(LOCATION_FILES_CONFIG);
                statefulSFTPClients.add(statefulSFTPClient);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("Created pool of StatefulSFTPClient size of {}", STATEFUL_SFTP_CLIENT_POOL);
        return statefulSFTPClients;
    }

    @PreDestroy
    public void destroySSHClients() {
        sshClientPool()
                .forEach(sshClient -> {
                    try {
                        sshClient.disconnect();
                    } catch (IOException e) {
                        log.error("Unable to disconnect SSHClient");
                    }
                });
        log.info("Disconnect all SSHClients");
    }

//    @Bean
//    public Consumer<EmailMessage> consumerFinishRegistrationBinding() {
//        return emailMessage ->
//                dossierService.finishRegistration(emailMessage);
//    }
//
//    @Bean
//    public Consumer<InformationEmailMessage> consumerCreateDocumentsBinding() {
//        return emailMessage ->
//            dossierService.createDocuments(emailMessage);
//    }
//
//    @Bean
//    public Consumer<EmailMessage> consumerSendDocumentsBinding() {
//        return emailMessage ->
//                dossierService.sendDocuments(emailMessage);
//    }
//
//    @Bean
//    public Consumer<SesEmailMessage> consumerSendSesCodeBinding() {
//        return emailMessage ->
//                dossierService.sendSesCode(emailMessage);
//    }
//
//    @Bean
//    public Consumer<EmailMessage> consumerSendIssuedCreditBinding() {
//        return emailMessage ->
//                dossierService.sendIssuedCreditEmail(emailMessage);
//    }
//
//    @Bean
//    public Consumer<EmailMessage> consumerApplicationDeniedBinding() {
//        return emailMessage ->
//                dossierService.sendApplicationDeniedEmail(emailMessage);
//    }
}
