package com.neoflex.creditconveyer.deal.domain.entity;

import com.neoflex.creditconveyer.deal.domain.enumeration.Gender;
import com.neoflex.creditconveyer.deal.domain.enumeration.MartialStatus;
import com.neoflex.creditconveyer.deal.domain.jsonb.EmploymentJsonb;
import com.neoflex.creditconveyer.deal.domain.jsonb.PassportJsonb;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "client")
@Getter
@Setter
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @NotNull
    @Column(name = "last_name")
    private String lastName;
    @NotNull
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @NotNull
    @Column(name = "birth_date")
    private Date birthdate;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    @Column(name = "martial_status")
    private MartialStatus martialStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull
    @Column(name = "passport_id")
    private PassportJsonb passport;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "employment_id")
    private EmploymentJsonb employment;

    private String account;

    @OneToOne(mappedBy = "client", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @PrimaryKeyJoinColumn
    private ApplicationEntity application;
}
