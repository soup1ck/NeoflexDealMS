package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.deal.data.dto.FinishRegistrationRequestDTO;
import ru.neoflex.deal.data.dto.ScoringDataDTO;

@Mapper(componentModel = "spring")
public interface FinishRegistrationScoringDataMapper {
    ScoringDataDTO finishRegistrationToScoring(FinishRegistrationRequestDTO finishRegistrationRequestDTO);
}
