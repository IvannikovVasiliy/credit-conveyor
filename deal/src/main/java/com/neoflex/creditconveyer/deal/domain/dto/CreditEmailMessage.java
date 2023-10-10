package com.neoflex.creditconveyer.deal.domain.dto;

import com.neoflex.creditconveyer.deal.domain.enumeration.Theme;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreditEmailMessage {
    private String address;
    private Theme theme;
    private Long applicationId;
    private CreditDTO credit;
}
