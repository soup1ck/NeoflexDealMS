package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.data.dto.CreditDTO;
import ru.neoflex.deal.data.enums.CreditStatus;
import ru.neoflex.deal.entity.Credit;
import ru.neoflex.deal.mapper.CreditMapper;
import ru.neoflex.deal.repository.CreditRepository;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;
    private final CreditMapper creditMapper;

    public Credit saveCreditInDb(Credit credit) {
        Credit creditInDb = creditRepository.save(credit);
        return creditInDb;
    }

    public Credit getCredit(CreditDTO creditDTO, CreditStatus creditStatus) {
        Credit credit = creditMapper.toCredit(creditDTO);
        credit.setCreditStatus(creditStatus.toString());
        return credit;
    }
}
