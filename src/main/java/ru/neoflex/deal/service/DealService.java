package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.data.dto.LoanApplicationRequestDTO;
import ru.neoflex.deal.entity.Application;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.mapper.LoanApplicationRequestClientMapper;
import ru.neoflex.deal.repository.ApplicationRepository;
import ru.neoflex.deal.repository.ClientRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {

    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;
    private final LoanApplicationRequestClientMapper clientMapper;

    public void handleLoanRequest(LoanApplicationRequestDTO loanApplicationRequestDTO){
        Client clientInDb = clientRepository.save(clientMapper.loanRequestToClient(loanApplicationRequestDTO));
        log.info(clientInDb.getClientId().toString());
        Application application = new Application();
        application.setClient(clientInDb);
        applicationRepository.save(application);
    }
}
