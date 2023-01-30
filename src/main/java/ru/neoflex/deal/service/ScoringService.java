package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.data.dto.FinishRegistrationRequestDTO;
import ru.neoflex.deal.data.dto.ScoringDataDTO;
import ru.neoflex.deal.data.jsonb.LoanOfferJsonb;
import ru.neoflex.deal.data.jsonb.PassportJsonb;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.mapper.FinishRegistrationRequestMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringService {

    private final FinishRegistrationRequestMapper finishRegistrationRequestMapper;

    public ScoringDataDTO createScoringData(LoanOfferJsonb appliedOffer,
                                            FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                            Client client) {
        log.info("Начат процесс формирования ScoringDataDTO");
        ScoringDataDTO scoringDataDTO = finishRegistrationRequestMapper.toScoringData(finishRegistrationRequestDTO,
                client);
        scoringDataDTO.setAmount(appliedOffer.getTotalAmount());
        scoringDataDTO.setTerm(appliedOffer.getTerm());
        scoringDataDTO.setIsInsuranceEnabled(appliedOffer.getIsInsuranceEnabled());
        scoringDataDTO.setIsSalaryClient(appliedOffer.getIsSalaryClient());
        log.info("ScoringDataDTO сформирован {}", scoringDataDTO);
        return scoringDataDTO;
    }
}
