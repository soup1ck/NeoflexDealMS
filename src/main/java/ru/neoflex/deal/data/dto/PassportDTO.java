package ru.neoflex.deal.data.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class PassportDTO {

    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
}
