package com.neoflex.creditconveyer.dossier.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class SFTPConfig {

    static Config config = ConfigFactory.load("conf/sftp.conf");

    public static String getHostsFile() {
        return config.getString("sshHostsFileName");
    }

    public static final Integer RESET_VALUE = 1_000_000;
}
