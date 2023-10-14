package com.neoflex.creditconveyer.dossier.domain.dto;

import com.neoflex.creditconveyer.dossier.domain.enumeration.Theme;
import com.neoflex.creditconveyer.dossier.domain.model.ApplicationModel;
import com.neoflex.creditconveyer.dossier.domain.model.ClientModel;
import com.neoflex.creditconveyer.dossier.domain.model.CreditModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InformationEmailMessage {
    private String address;
    private Theme theme;
    private Long applicationId;
    private ClientModel client;
    //private ApplicationModel application;
    //private CreditModel credit;
}
