package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.data.dto.*;
import ru.neoflex.deal.data.enums.ApplicationStatus;
import ru.neoflex.deal.data.enums.ChangeType;
import ru.neoflex.deal.data.enums.CreditStatus;
import ru.neoflex.deal.data.jsonb.LoanOfferJsonb;
import ru.neoflex.deal.entity.Application;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.entity.Credit;
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
    private final CreditService creditService;
    private final FeignControllerClient feignControllerClient;
    private final KafkaService kafkaService;
    private final GenerateSesCodeService generateSesCodeService;

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
        kafkaService.sendToFinishRegistrationTopic(loanOfferDTO.getApplicationId());
    }

    public void updateApplicationSesCode(Long applicationId) {
        int sesCode = generateSesCodeService.getRandomNumberInts();
        applicationService.updateApplicationSesCode(applicationId, sesCode);
        kafkaService.sendToSendSesTopic(applicationId);
    }

    public void verifySesCode(Long applicationId, Integer code){
        Application application = applicationService.getApplicationByApplicationId(applicationId);
        Integer sesCode = application.getSesCode();
        if(sesCode.equals(code)){
            applicationService.updateApplicationStatusHistory(application, ApplicationStatus.DOCUMENT_SIGNED,
                    ChangeType.AUTOMATIC);
            applicationService.updateApplicationStatusHistory(application, ApplicationStatus.CREDIT_ISSUED,
                    ChangeType.AUTOMATIC);
            kafkaService.sendToCreditIssuedTopic(applicationId);
        }
    }

    @Transactional
    public void calculateCredit(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                Long applicationId) {
        log.info("Процесс обработки FinishRequest начат, finishRequest: {} , applicationId: {}",
                finishRegistrationRequestDTO, applicationId);
        Application applicationInDb = applicationService.getApplicationByApplicationId(applicationId);
        Client updatedClient = clientService.updateClient(finishRegistrationRequestDTO, applicationId);
        applicationService.updateApplicationStatusHistory(applicationInDb, ApplicationStatus.CC_APPROVED,
                ChangeType.AUTOMATIC);
        LoanOfferJsonb appliedOffer = applicationInDb.getAppliedOffer();
        ScoringDataDTO scoringDataDTO = scoringService.createScoringData(appliedOffer, finishRegistrationRequestDTO,
                updatedClient);
        log.info("Процесс отправки запроса на МС КК для подсчета кредита начат");
        CreditDTO creditDTO = feignControllerClient.calculate(scoringDataDTO);
        log.info("Процесс отправки запроса на МС КК для подсчета кредита  закончен");
        Credit credit = creditService.getCredit(creditDTO, CreditStatus.CALCULATED);
        Credit creditInDb = creditService.saveCreditInDb(credit);
        applicationService.updateApplicationWithCredit(applicationInDb, creditInDb);
        log.info("Процесс обработки FinishRequest  закончен");
        kafkaService.sendToCreateDocumentsTopic(applicationId);
    }

    public void sendRequestForDocument(Long applicationId) {
        Application application = applicationService.getApplicationByApplicationId(applicationId);
        applicationService.updateApplicationStatusHistory(application,
                ApplicationStatus.PREPARE_DOCUMENTS, ChangeType.AUTOMATIC);
        kafkaService.sendToSendDocumentsTopic(applicationId);
    }

    public void updateApplicationStatus(Long applicationId, ApplicationStatus applicationStatus,
                                        ChangeType changeType) {
        Application application = applicationService.getApplicationByApplicationId(applicationId);
        applicationService.updateApplicationStatusHistory(application,
                applicationStatus, changeType);
    }
}
