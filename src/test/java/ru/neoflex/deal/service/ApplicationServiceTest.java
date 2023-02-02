package ru.neoflex.deal.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class ApplicationServiceTest {

    private final ApplicationRepository applicationRepository = Mockito.mock(ApplicationRepository.class);
    private final LoanOfferDTOJsonbMapper loanOfferMapper = Mockito.mock(LoanOfferDTOJsonbMapper.class);
    private final StatusHistoryMapper statusHistoryMapper = Mockito.mock(StatusHistoryMapper.class);
    private final ApplicationService applicationService =
            new ApplicationService(applicationRepository, loanOfferMapper, statusHistoryMapper);
    private Application application;

    @BeforeEach
    public void setUp() {
        Application application = new Application();
        application.setApplicationId(1L);
    }

    @Test
    @DisplayName("Получение предложения из бд по id")
    void getApplicationByApplicationId() {

        Mockito.when(applicationRepository.getApplicationByApplicationId(1L))
                .thenReturn(application);

        Application applicationByApplicationId = applicationService.getApplicationByApplicationId(1L);

        Assertions.assertEquals(applicationByApplicationId, application);
    }

    @Test
    @DisplayName("Получение предложения из бд по id с ex")
    void getApplicationByApplicationIdWithEx() {

        Mockito.when(applicationRepository.getApplicationByApplicationId(1L))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> applicationService.getApplicationByApplicationId(1L));
    }

    @Test
    @DisplayName(value = "Создание предложения в базе")
    void createAppInDb() {
        Application application = new Application();
        application.setApplicationId(1L);
        Client client = new Client();
        client.setClientId(1L);
        application.setClient(client);
        List<StatusHistoryJsonb> statusHistoryJsonbList = new ArrayList<>();
        ApplicationStatusHistoryDTO statusHistoryDTO = ApplicationStatusHistoryDTO.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .changeType(ChangeType.AUTOMATIC)
                .time(LocalDateTime.now())
                .build();
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setApplicationId(1L);

        Mockito.when(statusHistoryMapper.toJsonb(Mockito.any(ApplicationStatusHistoryDTO.class)))
                .thenAnswer(statusHistoryJsonb -> {
                    StatusHistoryJsonb statusHistoryJsonb1 = new StatusHistoryJsonb();
                    statusHistoryJsonb1.setStatus(statusHistoryDTO.getStatus().toString());
                    statusHistoryJsonb1.setTime(Timestamp.valueOf(statusHistoryDTO.getTime()));
                    statusHistoryJsonb1.setChangeType(statusHistoryDTO.getChangeType().toString());
                    return statusHistoryJsonb1;
                });

        statusHistoryJsonbList.add(statusHistoryMapper.toJsonb(statusHistoryDTO));
        application.setStatusHistory(statusHistoryJsonbList);

        Mockito.when(applicationRepository.save(Mockito.any(Application.class)))
                .thenReturn(application);

        Application appInDb = applicationService.createAppInDb(client);

        Assertions.assertEquals(ChangeType.AUTOMATIC.toString(), appInDb.getStatusHistory().get(0).getChangeType());
        Assertions.assertEquals(1L, appInDb.getClient().getClientId());
    }
}