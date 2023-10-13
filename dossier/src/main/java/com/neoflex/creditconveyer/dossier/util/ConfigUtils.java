package com.neoflex.creditconveyer.dossier.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigUtils {

    static Config config = ConfigFactory.load("conf/emailText.conf");

    public static String getTextFinishRegistration() {
        return config.getString("finishRegistration");
    }
    public static String getTextCreateDocuments() {
        return config.getString("createDocuments");
    }
    public static String getTextLoanAgreement() {
        return config.getString("loanAgreement");
    }
    public static String getTextQuestionnaire() {
        return config.getString("questionnaire");
    }
    public static String getTextPaymentSchedule() {
        return config.getString("paymentSchedule");
    }
    public static String getTextSesCode() {
        return config.getString("sesCode");
    }
    public static String getTextIssued() {
        return config.getString("issued");
    }
    public static String getTextApplicationDenied() {
        return config.getString("applicationDenied");
    }
}