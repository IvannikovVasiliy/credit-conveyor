package com.neoflex.creditconveyer.dossier.service;

import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessageDto;
import com.neoflex.creditconveyer.dossier.domain.dto.InformationEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.SesEmailMessageDto;

public interface DossierService {
    void finishRegistration(EmailMessageDto emailMessageDto);
    void createDocuments(InformationEmailMessage emailMessage);
    void sendDocuments(EmailMessageDto emailMessageDto);
    void sendSesCode(SesEmailMessageDto sesEmailMessageDto);
    void sendIssuedCreditEmail(EmailMessageDto sesEmailMessageDto);
    void sendApplicationDeniedEmail(EmailMessageDto sesEmailMessageDto);
}
