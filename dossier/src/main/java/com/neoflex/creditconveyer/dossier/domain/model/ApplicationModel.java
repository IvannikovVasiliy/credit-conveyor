package com.neoflex.creditconveyer.dossier.domain.model;

import com.neoflex.creditconveyer.dossier.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.dossier.domain.enumeration.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class ApplicationModel {
    private ApplicationStatus status;
    private Timestamp creationDate;
    private LoanOfferDTO appliedOffer;
    private List<StatusHistoryModel> statusHistory;
}