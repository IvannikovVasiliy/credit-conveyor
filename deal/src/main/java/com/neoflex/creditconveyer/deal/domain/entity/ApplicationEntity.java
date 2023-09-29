package com.neoflex.creditconveyer.deal.domain.entity;

import com.neoflex.creditconveyer.deal.domain.enumeration.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "client_id")
    private Long clientId;
    @Column(name = "credit_id")
    private Long creditId;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @Column(name = "creation_date")
    private Timestamp creationDate;

//    @Column(name = "applied_offer")
//    private Jsonb appliedOffer;

    @Column(name = "sign_date")
    private Timestamp signDate;

    @Column(name = "ses_code")
    private Integer sesCode;

//    @Column(name = "status_history")
//    private jsonb statusHistory;
}
