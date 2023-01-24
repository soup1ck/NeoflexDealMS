package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.data.dto.LoanApplicationRequestDTO;
import ru.neoflex.deal.data.dto.LoanOfferDTO;
import ru.neoflex.deal.data.enums.ApplicationStatus;
import ru.neoflex.deal.entity.Application;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.mapper.LoanApplicationRequestClientMapper;
import ru.neoflex.deal.mapper.LoanOfferDTOJsonbMapper;
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
    private final ConveyorUtils conveyorUtils;

    public List<LoanOfferDTO> handleLoanRequest(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Client clientInDb = clientRepository.save(clientMapper.loanRequestToClient(loanApplicationRequestDTO));
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

    public void updateApplication (LoanOfferDTO loanOfferDTO){
        Long applicationId = loanOfferDTO.getApplicationId();
        Application applicationInDb = applicationRepository.getApplicationByApplicationId(applicationId);
        applicationInDb.setStatus(ApplicationStatus.APPROVED.toString());
        applicationInDb.setAppliedOffer(loanOfferMapper.loanOfferDTOToJsonb(loanOfferDTO));
        applicationRepository.save(applicationInDb);
    }
}
