package com.neoflex.creditconveyer.dossier.domain.dto;

import com.neoflex.creditconveyer.dossier.domain.constant.Theme;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EM2 {
    private String address;
    private Theme theme;
    private Long applicationId;
}
