package com.neoflex.creditconveyer.deal.domain.jsonb;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
public class PassportJsonb {

    @JsonProperty("passport_id")
    @NotNull
    private UUID passportId;

    @Size(min = 4, max = 4)
    @NotNull
    private String series;
    @Size(min = 6, max = 6)
    @NotNull
    private String number;

    @Column(name = "issue_branch")
    private String issueBranch;
    @Column(name = "issue_date")
    private Date issueDate;
}
