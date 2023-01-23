package ru.neoflex.deal.data.jsonb;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PassportJsonb {

    private Long passportId;
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
}
