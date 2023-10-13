package com.neoflex.creditconveyer.dossier.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SftpConfig {

    @Value("${sftp.host}")
    private String SFTP_HOST;
    @Value("${sftp.port}")
    private Integer SFTP_PORT;
    @Value("${sftp.user}")
    private String SFTP_USER;
    @Value("${sftp.password}")
    private String SFTP_PASSWORD;
}
