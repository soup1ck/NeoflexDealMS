package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.data.dto.ApplicationStatusHistoryDTO;
import ru.neoflex.deal.data.dto.LoanOfferDTO;
import ru.neoflex.deal.data.enums.ApplicationStatus;
import ru.neoflex.deal.data.enums.ChangeType;
import ru.neoflex.deal.data.jsonb.StatusHistoryJsonb;
import ru.neoflex.deal.entity.Application;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.mapper.LoanOfferDTOJsonbMapper;
import ru.neoflex.deal.mapper.StatusHistoryMapper;
import ru.neoflex.deal.repository.ApplicationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final LoanOfferDTOJsonbMapper loanOfferMapper;
    private final StatusHistoryMapper statusHistoryMapper;

    public Application getApplicationByApplicationId(Long applicationId) {
        return applicationRepository.getApplicationByApplicationId(applicationId);
    }

    public Application createAppInDb(Client clientInDb) {
        log.info("Начат процесс создания Заявки");
        Application application = new Application();
        List<StatusHistoryJsonb> statusHistoryJsonbList = new ArrayList<>();
        application.setClient(clientInDb);
        StatusHistoryJsonb statusHistoryJsonb = createStatusHistoryJsonb(ApplicationStatus.APPROVED,
                ChangeType.AUTOMATIC);
        statusHistoryJsonbList.add(statusHistoryJsonb);
        application.setStatusHistory(statusHistoryJsonbList);
        application.setStatus(statusHistoryJsonb.getStatus());
        Application applicationInDb = applicationRepository.save(application);
        log.info("Application сохранен в бд {}", applicationInDb);
        return applicationInDb;
    }

    public void updateApplication(LoanOfferDTO loanOfferDTO) {
        log.info("Процесс обновления Application начат, принятое предложение {}", loanOfferDTO);
        Long applicationId = loanOfferDTO.getApplicationId();
        Application applicationInDb = applicationRepository.getApplicationByApplicationId(applicationId);
        List<StatusHistoryJsonb> statusHistory = applicationInDb.getStatusHistory();
        StatusHistoryJsonb statusHistoryJsonb = createStatusHistoryJsonb(ApplicationStatus.APPROVED,
                ChangeType.AUTOMATIC);
        statusHistory.add(statusHistoryJsonb);
        applicationInDb.setStatusHistory(statusHistory);
        applicationInDb.setStatus(statusHistoryJsonb.getStatus());
        applicationInDb.setAppliedOffer(loanOfferMapper.toJsonb(loanOfferDTO));
        Application updatedApp = applicationRepository.save(applicationInDb);
        log.info("Application обновлен и сохранен в бд {}", updatedApp);
    }

    private StatusHistoryJsonb createStatusHistoryJsonb(ApplicationStatus applicationStatus, ChangeType changeType) {
        ApplicationStatusHistoryDTO statusHistoryDTO = new ApplicationStatusHistoryDTO();
        statusHistoryDTO.setStatus(applicationStatus);
        statusHistoryDTO.setTime(LocalDateTime.now());
        statusHistoryDTO.setChangeType(changeType);
        StatusHistoryJsonb statusHistoryJsonb = statusHistoryMapper.toJsonb(statusHistoryDTO);
        return statusHistoryJsonb;
    }
}
