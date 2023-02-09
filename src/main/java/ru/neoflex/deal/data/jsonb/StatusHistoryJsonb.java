package ru.neoflex.deal.data.jsonb;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class StatusHistoryJsonb {

    private String status;
    private Timestamp time;
    private String changeType;
}
