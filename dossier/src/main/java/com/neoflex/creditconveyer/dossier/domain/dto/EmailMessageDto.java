package com.neoflex.creditconveyer.dossier.domain.dto;

import com.neoflex.creditconveyer.dossier.domain.enumeration.Theme;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EmailMessageDto implements Serializable {
    private String address;
    private Theme theme;
    private Long applicationId;
}

