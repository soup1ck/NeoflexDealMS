package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.deal.data.dto.FinishRegistrationRequestDTO;
import ru.neoflex.deal.data.dto.ScoringDataDTO;
import ru.neoflex.deal.entity.Client;

@Mapper(componentModel = "spring")
public interface ScoringDataMapper {

    @Mapping(source = "finishRegistrationRequestDTO.gender", target = "gender")
    @Mapping(source = "finishRegistrationRequestDTO.maritalStatus", target = "maritalStatus")
    @Mapping(source = "finishRegistrationRequestDTO.dependentAmount", target = "dependentAmount")
    @Mapping(source = "finishRegistrationRequestDTO.employment", target = "employment")
    @Mapping(source = "finishRegistrationRequestDTO.account", target = "account")
    ScoringDataDTO toScoringData(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                 Client client);
}
