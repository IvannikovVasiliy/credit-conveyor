package com.neoflex.creditconveyer.dossier.domain.dto;

import com.neoflex.creditconveyer.dossier.domain.enumeration.Theme;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SesEmailMessage {
    private String address;
    private Theme theme;
    private Long applicationId;
    private Integer sesCode;
}
