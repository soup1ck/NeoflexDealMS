package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.data.dto.FinishRegistrationRequestDTO;
import ru.neoflex.deal.data.dto.LoanApplicationRequestDTO;
import ru.neoflex.deal.data.dto.LoanOfferDTO;
import ru.neoflex.deal.data.dto.ScoringDataDTO;
import ru.neoflex.deal.data.jsonb.LoanOfferJsonb;
import ru.neoflex.deal.entity.Application;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.utils.FeignControllerClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {

    private final ClientService clientService;
    private final ApplicationService applicationService;
    private final ScoringService scoringService;
    private final LoanOfferService loanOfferService;
    private final FeignControllerClient feignControllerClient;

    @Transactional
    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Начат процесс обработки LoanRequest {}", loanApplicationRequestDTO);
        Client clientInDb = clientService.createClientInDb(loanApplicationRequestDTO);
        Application appInDb = applicationService.createAppInDb(clientInDb);
        Long applicationId = appInDb.getApplicationId();
        List<LoanOfferDTO> updatedLoanOffers = loanOfferService.getUpdatedLoanOffers(loanApplicationRequestDTO,
                applicationId);
        log.info("Процесс обработки LoanRequest завершен, предложения {}", updatedLoanOffers);
        return updatedLoanOffers;
    }

    public void updateApplication(LoanOfferDTO loanOfferDTO) {
        applicationService.updateApplication(loanOfferDTO);
    }

    @Transactional
    public void calculateCredit(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                Long applicationId) {
        log.info("Процесс обработки FinishRequest начат, finishRequest: {} , applicationId: {}",
                finishRegistrationRequestDTO, applicationId);
        Application applicationInDb = applicationService.getApplicationByApplicationId(applicationId);
        Client updatedClient = clientService.updateClient(finishRegistrationRequestDTO, applicationId);
        LoanOfferJsonb appliedOffer = applicationInDb.getAppliedOffer();
        ScoringDataDTO scoringDataDTO = scoringService.createScoringData(appliedOffer, finishRegistrationRequestDTO,
                updatedClient);
        log.info("Процесс отправки запроса на МС КК для подсчета кредита начат");
        feignControllerClient.calculate(scoringDataDTO);
        log.info("Процесс отправки запроса на МС КК для подсчета кредита  закончен");
        log.info("Процесс обработки FinishRequest  закончен");
    }
}
