package com.neoflex.creditconveyer.deal.domain.jsonb;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
public class PaymentScheduleElementJsonb implements Serializable {
    private Integer number;
    private Date date;
    private BigDecimal totalPayment;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;
}