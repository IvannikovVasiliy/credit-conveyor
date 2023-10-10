package com.neoflex.creditconveyer.deal.domain.dto;

import com.neoflex.creditconveyer.deal.domain.constant.Theme;
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
