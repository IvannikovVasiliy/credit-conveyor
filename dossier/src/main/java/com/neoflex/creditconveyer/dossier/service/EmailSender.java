package com.neoflex.creditconveyer.dossier.service;

import com.neoflex.creditconveyer.dossier.domain.model.CustomEmailMessage;
import org.springframework.core.io.ByteArrayResource;

import java.util.Map;

public interface EmailSender {
    void sendMail(CustomEmailMessage emailMessage);
    void sendMimeMail(CustomEmailMessage emailMessage, Map<String, ByteArrayResource> files);
}
