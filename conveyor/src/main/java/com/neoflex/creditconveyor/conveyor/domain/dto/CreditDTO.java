package com.neoflex.creditconveyor.conveyor.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CreditDTO {
      private BigDecimal amount;
      private Integer term;
      private BigDecimal monthlyPayment;
      private BigDecimal rate;
      private BigDecimal psk;
      private Boolean isInsuranceEnabled;
      private Boolean isSalaryClient;
      private List<PaymentScheduleElement> paymentSchedule;
}
