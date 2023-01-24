package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.deal.data.dto.LoanApplicationRequestDTO;
import ru.neoflex.deal.entity.Client;

@Mapper(componentModel = "spring")
public interface LoanApplicationRequestClientMapper {

    Client loanRequestToClient(LoanApplicationRequestDTO loanApplicationRequestDTO);
}
