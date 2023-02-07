package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.deal.data.dto.LoanOfferDTO;
import ru.neoflex.deal.data.jsonb.LoanOfferJsonb;

@Mapper(componentModel = "spring")
public interface LoanOfferDTOJsonbMapper {

    LoanOfferJsonb loanOfferDTOToJsonb(LoanOfferDTO loanOfferDTO);
}
