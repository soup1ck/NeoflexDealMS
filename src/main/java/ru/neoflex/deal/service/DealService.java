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
        Client client = clientMapper.loanRequestToClient(loanApplicationRequestDTO);
        PassportJsonb passport = new PassportJsonb();
        passport.setSeries(loanApplicationRequestDTO.getPassportSeries());
        passport.setNumber(loanApplicationRequestDTO.getPassportNumber());
        client.setPassport(passport);
        Client clientInDb = clientRepository.save(client);
        log.info(clientInDb.getClientId().toString());
        Application application = new Application();
        application.setClient(clientInDb);
        application.setStatus(ApplicationStatus.PREAPPROVAL.toString());
        Application applicationInDb = applicationRepository.save(application);
        Long applicationId = applicationInDb.getApplicationId();
        log.info(applicationId.toString());
        List<LoanOfferDTO> loanOffers = conveyorUtils.getLoanOffers(loanApplicationRequestDTO);
        for (LoanOfferDTO loanOffer : loanOffers) {
            loanOffer.setApplicationId(applicationId);
        }
        return loanOffers;
    }

    public void updateApplication(LoanOfferDTO loanOfferDTO) {
        Long applicationId = loanOfferDTO.getApplicationId();
        Application applicationInDb = applicationRepository.getApplicationByApplicationId(applicationId);
        applicationInDb.setStatus(ApplicationStatus.APPROVED.toString());
        applicationInDb.setAppliedOffer(loanOfferMapper.loanOfferDTOToJsonb(loanOfferDTO));
        applicationRepository.save(applicationInDb);
    }

    public void handleFinishRequest(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                    Long applicationId) {
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
        System.out.println(scoringDataDTO.toString());
        conveyorUtils.calculate(scoringDataDTO);
    }
}
