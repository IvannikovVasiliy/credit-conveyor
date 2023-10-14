package com.neoflex.creditconveyer.dossier.domain.model;

import com.neoflex.creditconveyer.dossier.domain.enumeration.MartialStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
    private PassportModel passport;
    private EmploymentModel employment;
    private String account;
}