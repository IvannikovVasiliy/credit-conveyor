package com.neoflex.creditconveyer.dossier.domain.dto;

import com.neoflex.creditconveyer.dossier.domain.constant.Theme;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EmailMessage implements Serializable {
    private String address;
    private Theme theme;
    private Long applicationId;
}

