package com.neoflex.creditconveyer.deal.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.sql.Date;

@Entity
@Table(name = "employment")
@Getter
@Setter
public class EmploymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passport_id")
    private Long id;

    @Size(min = 4, max = 4)
    private String series;
    @Size(min = 6, max = 6)
    private String number;

    @Column(name = "issue_branch")
    private String issueBranch;
    @Column(name = "issue_date")
    private Date issueDate;
}
