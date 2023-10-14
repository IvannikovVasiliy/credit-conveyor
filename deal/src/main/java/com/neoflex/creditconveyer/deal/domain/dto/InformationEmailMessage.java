package com.neoflex.creditconveyer.deal.domain.dto;

import com.neoflex.creditconveyer.deal.domain.enumeration.Theme;
import com.neoflex.creditconveyer.deal.domain.model.ApplicationModel;
import com.neoflex.creditconveyer.deal.domain.model.ClientModel;
import com.neoflex.creditconveyer.deal.domain.model.CreditModel;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InformationEmailMessage {
    @NotNull
    private String address;
    @NotNull
    private Theme theme;
    @NotNull
    private Long applicationId;
    private ClientModel client;
    @NotNull
    private ApplicationModel application;
    @NotNull
    private CreditModel credit;
}
