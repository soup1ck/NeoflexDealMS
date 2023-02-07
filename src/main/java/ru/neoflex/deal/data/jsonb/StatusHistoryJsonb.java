package ru.neoflex.deal.data.jsonb;

import lombok.*;

import java.sql.Timestamp;

@Data
public class StatusHistoryJsonb {

    private String status;
    private Timestamp time;
    private String changeType;
}
