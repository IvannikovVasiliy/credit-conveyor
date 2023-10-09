package com.neoflex.creditconveyer.dossier.service;

import com.neoflex.creditconveyer.dossier.domain.dto.CreditEmailMessage;
import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessage;

public interface DossierService {
    void finishRegistration(EmailMessage emailMessage);
    void createDocuments(CreditEmailMessage emailMessage);
    void sendDocuments(EmailMessage emailMessage);
}
