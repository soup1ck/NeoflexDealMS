package ru.neoflex.deal.data.jsonb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class EmploymentJsonb {

    private String status;
    private String employerInn;
    private BigDecimal salary;
    private String position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
