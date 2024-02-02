package com.neoflex.creditconveyer.deal.domain.entity;

import com.neoflex.creditconveyer.deal.domain.dto.LoanOfferDTO;
import com.neoflex.creditconveyer.deal.domain.enumeration.ApplicationStatus;
import com.neoflex.creditconveyer.deal.domain.jsonb.StatusHistoryJsonb;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "application")
@Getter
@Setter
public class ApplicationEntity {

    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private ClientEntity client;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "credit_id", referencedColumnName = "credit_id")
    private CreditEntity credit;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ApplicationStatus status;

    @Column(name = "creation_date")
    @CreationTimestamp
    @NotNull
    private Timestamp creationDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "applied_offer")
    private LoanOfferDTO appliedOffer;

    @Column(name = "sign_date")
    private Timestamp signDate;

    @Column(name = "ses_code")
    private Integer sesCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    @Column(name = "status_history")
    private List<StatusHistoryJsonb> statusHistory;
}
