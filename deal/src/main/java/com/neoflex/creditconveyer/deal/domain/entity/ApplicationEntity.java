package com.neoflex.creditconveyer.deal.domain.entity;

import com.neoflex.creditconveyer.deal.domain.enumeration.ApplicationStatus;
import com.neoflex.creditconveyer.deal.domain.jsonb.AppliedOfferJsonb;
import com.neoflex.creditconveyer.deal.domain.jsonb.StatusHistoryJsonb;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "application")
@Getter
@Setter
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @NotNull
    @JoinColumn(name = "client_id")
    private ClientEntity client;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "credit_id")
    private CreditEntity credit;

    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false)
    private ApplicationStatus status;

    @Column(name = "creation_date")
    @NotNull
    private Timestamp creationDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "applied_offer")
    private AppliedOfferJsonb appliedOffer;

    @Column(name = "sign_date")
    private Timestamp signDate;

    @Column(name = "ses_code")
    private Integer sesCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "status_history")
    private StatusHistoryJsonb statusHistory;
}
