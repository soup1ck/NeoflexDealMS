package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.data.dto.*;
import ru.neoflex.deal.data.enums.ApplicationStatus;
import ru.neoflex.deal.data.jsonb.EmploymentJsonb;
import ru.neoflex.deal.data.jsonb.LoanOfferJsonb;
import ru.neoflex.deal.data.jsonb.PassportJsonb;
import ru.neoflex.deal.entity.Application;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.mapper.EmploymentMapper;
import ru.neoflex.deal.mapper.LoanApplicationRequestClientMapper;
import ru.neoflex.deal.mapper.LoanOfferDTOJsonbMapper;
import ru.neoflex.deal.mapper.ScoringDataMapper;
import ru.neoflex.deal.repository.ApplicationRepository;
import ru.neoflex.deal.repository.ClientRepository;
import ru.neoflex.deal.utils.ConveyorUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {

    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;
    private final LoanApplicationRequestClientMapper clientMapper;
    private final LoanOfferDTOJsonbMapper loanOfferMapper;
    private final ScoringDataMapper scoringDataMapper;
    private final EmploymentMapper employmentMapper;
    private final ConveyorUtils conveyorUtils;

    public List<LoanOfferDTO> handleLoanRequest(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Начат процесс обработки LoanRequest");
        Client client = clientMapper.loanRequestToClient(loanApplicationRequestDTO);
        PassportJsonb passport = new PassportJsonb();
        passport.setSeries(loanApplicationRequestDTO.getPassportSeries());
        passport.setNumber(loanApplicationRequestDTO.getPassportNumber());
        client.setPassport(passport);
        Client clientInDb = clientRepository.save(client);
        log.info("Client сохранени в базу данных");
        log.info(clientInDb.toString());
        Application application = new Application();
        application.setClient(clientInDb);
        application.setStatus(ApplicationStatus.PREAPPROVAL.toString());
        Application applicationInDb = applicationRepository.save(application);
        log.info("Application сохранен в бд");
        log.info(applicationInDb.toString());
        Long applicationId = applicationInDb.getApplicationId();
        log.info("Процесс отправки запроса на МС КК начат");
        List<LoanOfferDTO> loanOffers = conveyorUtils.getLoanOffers(loanApplicationRequestDTO);
        log.info("Процесс отправки запроса на МС КК закончен");
        for (LoanOfferDTO loanOffer : loanOffers) {
            loanOffer.setApplicationId(applicationId);
        }
        log.info("Процесс обработки LoanRequest завершен");
        log.info(loanOffers.toString());
        return loanOffers;
    }

    public void updateApplication(LoanOfferDTO loanOfferDTO) {
        log.info("Процесс обновления Application начат");
        Long applicationId = loanOfferDTO.getApplicationId();
        Application applicationInDb = applicationRepository.getApplicationByApplicationId(applicationId);
        applicationInDb.setStatus(ApplicationStatus.APPROVED.toString());
        applicationInDb.setAppliedOffer(loanOfferMapper.loanOfferDTOToJsonb(loanOfferDTO));
        applicationRepository.save(applicationInDb);
        log.info("Application обновлен и сохранен в бд");
        log.info(applicationInDb.toString());
    }

    public void handleFinishRequest(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                    Long applicationId) {
        log.info("Процесс обработки FinishRequest начат");
        Application applicationInDb = applicationRepository.getApplicationByApplicationId(applicationId);
        Client clientInDb = applicationInDb.getClient();
        PassportJsonb passport = clientInDb.getPassport();
        passport.setIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());
        passport.setIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());
        EmploymentDTO employmentDTO = finishRegistrationRequestDTO.getEmployment();
        EmploymentJsonb employmentJsonb = employmentMapper.DTOToJsonb(employmentDTO);
        clientInDb.setEmployment(employmentJsonb);
        Client updatedClient = clientRepository.save(clientInDb);
        LoanOfferJsonb appliedOffer = applicationInDb.getAppliedOffer();
        ScoringDataDTO scoringDataDTO = scoringDataMapper.toScoringData(finishRegistrationRequestDTO,
                clientInDb);
        scoringDataDTO.setAmount(appliedOffer.getRequestedAmount());
        scoringDataDTO.setTerm(appliedOffer.getTerm());
        scoringDataDTO.setPassportNumber(updatedClient.getPassport().getNumber());
        scoringDataDTO.setPassportSeries(updatedClient.getPassport().getSeries());
        scoringDataDTO.setIsInsuranceEnabled(appliedOffer.getIsInsuranceEnabled());
        scoringDataDTO.setIsSalaryClient(appliedOffer.getIsSalaryClient());
        log.info("ScoringDataDTO сформирован");
        log.info(scoringDataDTO.toString());
        log.info("Процесс отправки запроса на МС КК начат");
        conveyorUtils.calculate(scoringDataDTO);
        log.info("Процесс отправки запроса на МС КК закончен");
    }
}
