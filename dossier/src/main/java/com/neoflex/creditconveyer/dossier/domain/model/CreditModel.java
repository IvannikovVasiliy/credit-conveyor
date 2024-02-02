package com.neoflex.creditconveyer.dossier.domain.model;

import com.neoflex.creditconveyer.dossier.domain.dto.PaymentScheduleElementJsonb;
import com.neoflex.creditconveyer.dossier.domain.enumeration.CreditStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreditModel {
    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    private List<PaymentScheduleElementJsonb> paymentSchedule;
    private Boolean insuranceEnable;
    private Boolean salaryClient;
    private CreditStatus creditStatus;
}