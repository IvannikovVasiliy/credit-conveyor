package com.neoflex.creditconveyer.dossier.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class SecretConfig {

    static Config secretConfig = ConfigFactory.load("conf/secrets.conf");

    public static String getMailPassword() {
        return secretConfig.getString("mail-password");
    }
}
