package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.data.dto.LoanOfferDTO;
import ru.neoflex.deal.data.enums.ApplicationStatus;
import ru.neoflex.deal.entity.Application;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.mapper.LoanOfferDTOJsonbMapper;
import ru.neoflex.deal.repository.ApplicationRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final LoanOfferDTOJsonbMapper loanOfferMapper;

    public Application getApplicationByApplicationId(Long applicationId){
        return applicationRepository.getApplicationByApplicationId(applicationId);
    }

    public Application createAppInDb(Client clientInDb) {
        log.info("Начат процесс создания Заявки");
        Application application = new Application();
        application.setClient(clientInDb);
        application.setStatus(ApplicationStatus.PREAPPROVAL.toString());
        Application applicationInDb = applicationRepository.save(application);
        log.info("Application сохранен в бд {}", applicationInDb);
        return applicationInDb;
    }

    public void updateApplication(LoanOfferDTO loanOfferDTO) {
        log.info("Процесс обновления Application начат, принятое предложение {}", loanOfferDTO);
        Long applicationId = loanOfferDTO.getApplicationId();
        Application applicationInDb = applicationRepository.getApplicationByApplicationId(applicationId);
        applicationInDb.setStatus(ApplicationStatus.APPROVED.toString());
        applicationInDb.setAppliedOffer(loanOfferMapper.toJsonb(loanOfferDTO));
        Application updatedApp = applicationRepository.save(applicationInDb);
        log.info("Application обновлен и сохранен в бд {}", updatedApp);
    }
}
