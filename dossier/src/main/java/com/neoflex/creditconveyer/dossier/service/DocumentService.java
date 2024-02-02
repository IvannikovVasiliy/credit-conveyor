package com.neoflex.creditconveyer.dossier.service;

import com.neoflex.creditconveyer.dossier.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.dossier.domain.dto.PaymentScheduleElementJsonb;
import com.neoflex.creditconveyer.dossier.domain.model.ApplicationModel;
import com.neoflex.creditconveyer.dossier.domain.model.ClientModel;
import com.neoflex.creditconveyer.dossier.domain.model.CreditModel;
import com.neoflex.creditconveyer.dossier.domain.model.DocumentModel;

import java.util.List;

public interface DocumentService {
    DocumentModel createLoanAgreement(Long applicationId, ClientModel client, ApplicationModel application, CreditModel credit);
    DocumentModel createQuestionnaire(Long applicationId, ClientModel client, LoanOfferDTO loanOffer, CreditModel credit);
    DocumentModel createPaymentSchedule(Long applicationId, List<PaymentScheduleElementJsonb> paymentSchedule);
}
