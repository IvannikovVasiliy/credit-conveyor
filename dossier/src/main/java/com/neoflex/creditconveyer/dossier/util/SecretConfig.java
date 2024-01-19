package com.neoflex.creditconveyer.dossier.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class SecretConfig {

    static Config secretConfig = ConfigFactory.load("conf/secrets.conf");

    public static String getMailPasswordConfig() {
        return secretConfig.getString("mailPassword");
    }

    public static String getSftpUserConfig() {
        return secretConfig.getString("sftpUser");
    }
    public static String getSftpPasswordConfig() {
        return secretConfig.getString("sftpPassword");
    }
}
