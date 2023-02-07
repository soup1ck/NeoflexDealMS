package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;
    private final LoanApplicationRequestMapper loanApplicationRequestMapper;
    private final PassportMapper passportMapper;
    private final EmploymentMapper employmentMapper;

    public Client createClientInDb(LoanApplicationRequestDTO loanApplicationRequestDTO){
        log.info("Начат процесс создания Клиента");
        Client client = loanApplicationRequestMapper.toClient(loanApplicationRequestDTO);
        PassportDTO passportDTO = new PassportDTO();
        passportDTO.setSeries(loanApplicationRequestDTO.getPassportSeries());
        passportDTO.setNumber(loanApplicationRequestDTO.getPassportNumber());
        PassportJsonb passportJsonb = passportMapper.toJsonb(passportDTO);
        client.setPassport(passportJsonb);
        Client clientInDb = clientRepository.save(client);
        log.info("Client сохранени в базу данных {}", clientInDb);
        return clientInDb;
    }

    public Client updateClient(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                  Long applicationId){
        log.info("Процесс обновления Клиента начат");
        Application applicationInDb = applicationRepository.getApplicationByApplicationId(applicationId);
        Client clientInDb = applicationInDb.getClient();
        log.info("Клиент {}", clientInDb);
        PassportJsonb passportJsonb = clientInDb.getPassport();
        passportJsonb.setIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());
        passportJsonb.setIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());
        EmploymentDTO employmentDTO = finishRegistrationRequestDTO.getEmployment();
        EmploymentJsonb employmentJsonb = employmentMapper.DTOToJsonb(employmentDTO);
        clientInDb.setEmployment(employmentJsonb);
        Client updatedClient = clientRepository.save(clientInDb);
        log.info("Процесс обновления Клиента завершен, обновленный Клиент: {}", updatedClient);
        return updatedClient;
    }
}
