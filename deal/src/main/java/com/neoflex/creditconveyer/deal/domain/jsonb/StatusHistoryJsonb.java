package com.neoflex.creditconveyer.deal.domain.jsonb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.neoflex.creditconveyer.deal.domain.enumeration.ChangeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Embeddable
@Table(name = "status_history")
@Getter
@Setter
public class StatusHistoryJsonb {
    private String status;
    private Timestamp time;

    @JsonProperty("change_type")
    private ChangeType changeType;
}
