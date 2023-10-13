package com.neoflex.creditconveyer.dossier.service;

import com.neoflex.creditconveyer.dossier.domain.dto.CreditDTO;
import com.neoflex.creditconveyer.dossier.domain.model.DocumentModel;

public interface DocumentService {
    DocumentModel createLoanAgreement(Long applicationId, CreditDTO creditDTO);
}
