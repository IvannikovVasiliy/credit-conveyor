package com.neoflex.creditconveyer.deal.domain.model;

import com.neoflex.creditconveyer.deal.domain.enumeration.MartialStatus;
import com.neoflex.creditconveyer.deal.domain.jsonb.EmploymentJsonb;
import com.neoflex.creditconveyer.deal.domain.jsonb.PassportJsonb;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class ClientModel {
    private String lastName;
    private String firstName;
    private String middleName;
    private Date birthdate;
    private String email;
    private MartialStatus martialStatus;
    private Integer dependentAmount;
    private PassportJsonb passport;
    private EmploymentJsonb employment;
    private String account;
}
