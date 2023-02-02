package ru.neoflex.deal.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.neoflex.deal.data.dto.EmploymentDTO;
import ru.neoflex.deal.data.dto.FinishRegistrationRequestDTO;
import ru.neoflex.deal.data.dto.LoanApplicationRequestDTO;
import ru.neoflex.deal.data.dto.PassportDTO;
import ru.neoflex.deal.data.jsonb.EmploymentJsonb;
import ru.neoflex.deal.data.jsonb.PassportJsonb;
import ru.neoflex.deal.entity.Application;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.mapper.EmploymentMapper;
import ru.neoflex.deal.mapper.LoanApplicationRequestMapper;
import ru.neoflex.deal.mapper.PassportMapper;
import ru.neoflex.deal.repository.ApplicationRepository;
import ru.neoflex.deal.repository.ClientRepository;

import java.time.LocalDate;

class ClientServiceTest {

    private final ClientRepository clientRepository = Mockito.mock(ClientRepository.class);
    private final ApplicationRepository applicationRepository = Mockito.mock(ApplicationRepository.class);
    private final LoanApplicationRequestMapper loanApplicationRequestMapper = Mockito
            .mock(LoanApplicationRequestMapper.class);
    private final PassportMapper passportMapper = Mockito.mock(PassportMapper.class);
    private final EmploymentMapper employmentMapper = Mockito.mock(EmploymentMapper.class);
    private final ClientService clientService = new ClientService(clientRepository, applicationRepository,
            loanApplicationRequestMapper, passportMapper, employmentMapper);

    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client();
        client.setClientId(1L);
    }

    @Test
    @DisplayName(value = "Создание клиента в базе")
    void createClientInDb() {

        LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO();
        loanApplicationRequestDTO.setPassportNumber("test");
        loanApplicationRequestDTO.setPassportSeries("test");

        Mockito.when(loanApplicationRequestMapper.toClient(Mockito.any(LoanApplicationRequestDTO.class)))
                .thenAnswer(mockObject -> {
                    client.setPassport(passportMapper.toJsonb(Mockito.any(PassportDTO.class)));
                    return client;
                });
        Mockito.when(passportMapper.toJsonb(Mockito.any(PassportDTO.class)))
                .thenAnswer(mockObject -> {
                    PassportJsonb passportJsonb = new PassportJsonb();
                    passportJsonb.setSeries(loanApplicationRequestDTO.getPassportSeries());
                    passportJsonb.setNumber(loanApplicationRequestDTO.getPassportNumber());
                    return passportJsonb;
                });
        Mockito.when(clientRepository.save(client)).thenReturn(client);

        clientService.createClientInDb(loanApplicationRequestDTO);

        Assertions.assertEquals("test", client.getPassport().getSeries());
        Assertions.assertEquals("test", client.getPassport().getNumber());
    }

    @Test
    @DisplayName(value = "Обновление клиента")
    void updateClient() {

        Application application = new Application();
        PassportJsonb passportJsonb = new PassportJsonb();
        passportJsonb.setSeries("test");
        passportJsonb.setNumber("test");
        client.setPassport(passportJsonb);
        application.setClient(client);
        application.setApplicationId(1L);
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = new FinishRegistrationRequestDTO();
        EmploymentDTO employmentDTO = new EmploymentDTO();
        employmentDTO.setEmployerINN("1234");
        finishRegistrationRequestDTO.setPassportIssueBranch("test");
        finishRegistrationRequestDTO.setPassportIssueDate(LocalDate.now());
        finishRegistrationRequestDTO.setEmployment(employmentDTO);

        Mockito.when(applicationRepository.getApplicationByApplicationId(1L))
                .thenReturn(application);
        Mockito.when(employmentMapper.DTOToJsonb(employmentDTO))
                .thenAnswer(mockObj -> {
                    EmploymentJsonb employmentJsonb = new EmploymentJsonb();
                    employmentJsonb.setEmployerInn("1234");
                    return employmentJsonb;
                });
        Mockito.when(clientRepository.save(client)).thenReturn(client);

        Client updatedClient = clientService.updateClient(finishRegistrationRequestDTO, 1L);

        Assertions.assertEquals("1234", updatedClient.getEmployment().getEmployerInn());
    }
}