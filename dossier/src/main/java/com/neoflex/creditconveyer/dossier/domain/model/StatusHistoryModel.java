package com.neoflex.creditconveyer.dossier.domain.model;

import com.neoflex.creditconveyer.dossier.domain.enumeration.ChangeType;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class StatusHistoryModel {

    private String status;
    private Timestamp time;
    private ChangeType changeType;
}