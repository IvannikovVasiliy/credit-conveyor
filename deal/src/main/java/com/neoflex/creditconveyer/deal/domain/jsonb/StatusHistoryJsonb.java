package com.neoflex.creditconveyer.deal.domain.jsonb;

import com.neoflex.creditconveyer.deal.domain.enumeration.ChangeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class StatusHistoryJsonb {
    private String status;
    private Timestamp time;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type")
    private ChangeType changeType;
}
