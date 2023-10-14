package com.neoflex.creditconveyer.dossier.domain.jsonb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class PassportJsonb {
    @JsonProperty("passport_id")
    private UUID passportId;
    private String series;
    private String number;
    private String issueBranch;
    private Date issueDate;
}