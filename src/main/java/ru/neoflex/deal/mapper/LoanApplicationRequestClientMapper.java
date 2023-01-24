package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.deal.data.dto.LoanApplicationRequestDTO;
import ru.neoflex.deal.entity.Client;

@Mapper(componentModel = "spring")
public interface LoanApplicationRequestClientMapper {

    @Mapping(target = "clientId", ignore = true)
    Client loanRequestToClient(LoanApplicationRequestDTO loanApplicationRequestDTO);
}
