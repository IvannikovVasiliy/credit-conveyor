package com.neoflex.creditconveyer.dossier.service;

import com.neoflex.creditconveyer.dossier.domain.dto.InformationEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.SesEmailMessage;

public interface DossierService {
    void finishRegistration(EmailMessage emailMessage);
    void createDocuments(InformationEmailMessage emailMessage);
    void sendDocuments(EmailMessage emailMessage);
    void sendSesCode(SesEmailMessage sesEmailMessage);
    void sendIssuedCreditEmail(EmailMessage sesEmailMessage);
    void sendApplicationDeniedEmail(EmailMessage sesEmailMessage);
}
