package ru.neoflex.deal.data.jsonb;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class StatusHistoryJsonb {

    private String status;
    private Timestamp time;
    private String changeType;
}
