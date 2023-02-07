package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.deal.data.dto.FinishRegistrationRequestDTO;
import ru.neoflex.deal.data.dto.ScoringDataDTO;
import ru.neoflex.deal.entity.Client;

@Mapper(componentModel = "spring")
public interface FinishRegistrationRequestMapper {

    @Mapping(source = "finishRegistrationRequestDTO.gender", target = "gender")
    @Mapping(source = "finishRegistrationRequestDTO.maritalStatus", target = "maritalStatus")
    @Mapping(source = "finishRegistrationRequestDTO.dependentAmount", target = "dependentAmount")
    @Mapping(source = "finishRegistrationRequestDTO.employment", target = "employment")
    @Mapping(source = "finishRegistrationRequestDTO.account", target = "account")
    @Mapping(source = "client.passport.number", target = "passportNumber")
    @Mapping(source = "client.passport.series", target = "passportSeries")
    @Mapping(source = "client.passport.issueDate", target = "passportIssueDate")
    @Mapping(source = "client.passport.issueBranch", target = "passportIssueBranch")
    ScoringDataDTO toScoringData(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                 Client client);
}
