package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.deal.data.dto.ScoringDataDTO;
import ru.neoflex.deal.entity.Client;

@Mapper(componentModel = "spring")
public interface ClientScoringDataMapper {

    ScoringDataDTO clientToScoringData(Client client);
}
