package com.neoflex.creditconveyer.deal.domain.entity;

import com.neoflex.creditconveyer.deal.domain.enumeration.ChangeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import javax.naming.Name;
import java.sql.Timestamp;

@Embeddable
@Table(name = "status_history")
@Getter
@Setter
public class StatusHistoryEntity {
    private String status;
    private Timestamp time;
    private ChangeType changeType;
}
