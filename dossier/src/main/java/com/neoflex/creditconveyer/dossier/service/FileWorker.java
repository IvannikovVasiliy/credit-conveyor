package com.neoflex.creditconveyer.dossier.service;

import net.schmizz.sshj.sftp.StatefulSFTPClient;

public interface FileWorker {
    void writeFileInRemoteServer(StatefulSFTPClient statefulSFTPClient, String fileText, String fileName);
}
