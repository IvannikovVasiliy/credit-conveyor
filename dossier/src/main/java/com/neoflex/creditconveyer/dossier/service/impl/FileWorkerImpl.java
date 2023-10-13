package com.neoflex.creditconveyer.dossier.service.impl;

import com.neoflex.creditconveyer.dossier.service.FileWorker;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.sftp.OpenMode;
import net.schmizz.sshj.sftp.RemoteFile;
import net.schmizz.sshj.sftp.StatefulSFTPClient;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;

@Service
@Slf4j
public class FileWorkerImpl implements FileWorker {

    public void writeFileInRemoteServer(StatefulSFTPClient statefulSFTPClient, String fileText, String fileName) {
        log.debug("Input writeFileInRemoteServer");

        ByteArrayInputStream loanInputStream = new ByteArrayInputStream(fileText.getBytes());
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
