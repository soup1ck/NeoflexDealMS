package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.deal.data.dto.CreditDTO;
import ru.neoflex.deal.entity.Credit;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    @Mapping(target = "insuranceEnable", source = "creditDTO.isInsuranceEnabled")
    @Mapping(target = "salaryClient", source = "creditDTO.isSalaryClient")
    Credit toCredit(CreditDTO creditDTO);
}
