package com.neoflex.creditconveyer.deal.service;

import com.neoflex.creditconveyer.deal.domain.dto.VerifyCodeDTO;

public interface DocumentService {
    void sendDocuments(Long applicationId);
    void signDocuments(Long applicationId);
    void issuedCredit(Long applicationId, VerifyCodeDTO verifyCodeDTO);
}
