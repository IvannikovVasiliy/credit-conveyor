package com.neoflex.creditconveyer.deal.service;

public interface DocumentService {
    void sendDocuments(Long applicationId);
    void signDocuments(Long applicationId);
}
