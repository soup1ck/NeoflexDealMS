package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.neoflex.deal.data.dto.PassportDTO;
import ru.neoflex.deal.data.jsonb.PassportJsonb;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PassportMapper {

    PassportJsonb toJsonb(PassportDTO passportDTO);
}
