package com.neoflex.creditconveyer.dossier.domain.dto;

import com.neoflex.creditconveyer.dossier.domain.enumeration.Theme;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditEmailMessage {
    private String address;
    private Theme theme;
    private Long applicationId;
    private CreditDTO credit;
}
