package com.neoflex.creditconveyer.deal.domain.entity;

import com.neoflex.creditconveyer.deal.domain.enumeration.CreditStatus;
import com.neoflex.creditconveyer.deal.domain.jsonb.PaymentScheduleElementJsonb;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "credit")
@Getter
@Setter
public class CreditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_id")
    private Long id;

    @NotNull
    private BigDecimal amount;
    @NotNull
    private Integer term;
    @NotNull
    private BigDecimal monthlyPayment;
    @NotNull
    private BigDecimal rate;
    @NotNull
    private BigDecimal psk;

    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    @Column(name = "payment_schedule")
    private List<PaymentScheduleElementJsonb> paymentSchedule;

    @NotNull
    private Boolean insuranceEnable;
    @NotNull
    @Column(name = "salary_client")
    private Boolean salaryClient;

    @Enumerated(EnumType.STRING)
    @Column(name = "credit_status")
    private CreditStatus creditStatus;

    @OneToOne(mappedBy = "credit", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private ApplicationEntity application;
}
