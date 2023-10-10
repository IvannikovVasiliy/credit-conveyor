package com.neoflex.creditconveyer.deal.domain.dto;

import com.neoflex.creditconveyer.deal.domain.enumeration.Theme;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreditEmailMessage {
    @NotNull
    private String address;
    @NotNull
    private Theme theme;
    @NotNull
    private Long applicationId;
    @NotNull
    private CreditDTO credit;
}
