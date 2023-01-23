package ru.neoflex.deal.data.jsonb;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EmploymentJsonb {

    private Long employmentId;
    private String status;
    private String employerInn;
    private BigDecimal salary;
    private String position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
