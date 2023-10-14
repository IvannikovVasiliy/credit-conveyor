package com.neoflex.creditconveyer.dossier.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "document")
@Getter
@Setter
public class DocumentEntity {

    public DocumentEntity(Long id, String loanAgreementName, String questionnaireName, String paymentScheduleName) {
        this.id = id;
        this.loanAgreementName = loanAgreementName;
        this.questionnaireName = questionnaireName;
        this.paymentScheduleName = paymentScheduleName;
    }

    public DocumentEntity() {
    }

    @Id
    @Column(name = "application_id")
    private Long id;

    @NotNull
    @Column(name = "loan_agreement_name")
    private String loanAgreementName;
    @NotNull
    @Column(name = "questionnaire_name")
    private String questionnaireName;
    @NotNull
    @Column(name = "payment_schedule_name")
    private String paymentScheduleName;
}
