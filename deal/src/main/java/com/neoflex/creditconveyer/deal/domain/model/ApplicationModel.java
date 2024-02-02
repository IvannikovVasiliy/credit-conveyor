package com.neoflex.creditconveyer.deal.domain.model;

import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.domain.enumeration.ApplicationStatus;
import com.neoflex.creditconveyer.deal.domain.jsonb.StatusHistoryJsonb;
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
    private List<StatusHistoryJsonb> statusHistory;
}
