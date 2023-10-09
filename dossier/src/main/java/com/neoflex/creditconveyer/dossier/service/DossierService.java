package com.neoflex.creditconveyer.dossier.service;

import com.neoflex.creditconveyer.dossier.domain.dto.EmailMessage;

public interface DossierService {
    void finishRegistration(EmailMessage emailMessage);
    void createDocuments(EmailMessage emailMessage);
}
