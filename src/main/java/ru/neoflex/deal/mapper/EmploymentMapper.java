package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.deal.data.dto.EmploymentDTO;
import ru.neoflex.deal.data.jsonb.EmploymentJsonb;

@Mapper(componentModel = "spring")
public interface EmploymentMapper {

    @Mapping(target = "status", source = "employmentDTO.employmentStatus")
    @Mapping(target = "employerInn", source = "employmentDTO.employerINN")
    EmploymentJsonb DTOToJsonb(EmploymentDTO employmentDTO);
}
