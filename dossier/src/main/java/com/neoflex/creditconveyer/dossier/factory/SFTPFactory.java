package com.neoflex.creditconveyer.dossier.factory;

import com.neoflex.creditconveyer.dossier.util.SFTPConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.StatefulSFTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SFTPFactory {

    @Autowired
    public SFTPFactory(List<SSHClient> sshClients, List<StatefulSFTPClient> statefulSFTPClients) {
        this.sshClients = sshClients;
        this.statefulSFTPClients = statefulSFTPClients;
    }

    @Value("${sftp.sshClientPool}")
    private Integer SSH_CLIENT_POOL;
    @Value("${sftp.statefulSftpClientPool}")
    private Integer STATEFUL_SFTP_CLIENT_POOL;

    private final List<SSHClient> sshClients;
    private final List<StatefulSFTPClient> statefulSFTPClients;
    private final AtomicInteger counterSSHClients = new AtomicInteger();
    private final AtomicInteger counterSFTPClients = new AtomicInteger();

    public SSHClient getSshClient() {
        int index = counterSSHClients.incrementAndGet();
        if (counterSSHClients.get() >= SFTPConfig.RESET_VALUE) {
            counterSSHClients.set(0);
        }
        return sshClients.get(index % SSH_CLIENT_POOL);
    }

    public StatefulSFTPClient getStatefulSFTPClient() {
        int index = counterSFTPClients.incrementAndGet();
        if (counterSFTPClients.get() >= SFTPConfig.RESET_VALUE) {
            counterSFTPClients.set(0);
        }
        return statefulSFTPClients.get(index % STATEFUL_SFTP_CLIENT_POOL);
    }
}
