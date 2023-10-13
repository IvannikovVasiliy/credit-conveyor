package com.neoflex.creditconveyer.dossier.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigUtil {

    static Config config = ConfigFactory.load();

    public static String getHostsFileName() {
        return config.getString("hostsFile");
    }
}
