package com.neoflex.creditconveyer.deal.domain.entity;

import com.neoflex.creditconveyer.deal.domain.enumeration.Gender;
import com.neoflex.creditconveyer.deal.domain.enumeration.MartialStatus;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "client")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "birth_date")
    private Date birthdate;
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated
    @Column(name = "martial_status")
    private MartialStatus martialStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @Column(name = "passport_id")

    private Long passportId;

    @Column(name = "employment_id")
    private Long employmentId;

    private String account;
}
