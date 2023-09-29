package com.neoflex.creditconveyer.deal.domain.entity;

import com.neoflex.creditconveyer.deal.domain.enumeration.CreditStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "credit")
@Getter
@Setter
public class CreditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_id")
    private Long id;

    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;

//    @Column(name = "payment_schedule")
//    private jsonb paymentSchedule;

    private Boolean insuranceEnable;
    @Column(name = "salary_client")
    private Boolean salaryClient;

    @Column(name = "credit_status")
    private CreditStatus creditStatus;
}
