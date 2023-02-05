package ru.neoflex.deal.utils;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.deal.data.dto.CreditDTO;
import ru.neoflex.deal.data.dto.LoanApplicationRequestDTO;
import ru.neoflex.deal.data.dto.LoanOfferDTO;
import ru.neoflex.deal.data.dto.ScoringDataDTO;

import java.util.List;

@FeignClient(value = "deal", url = "http://localhost:8080/conveyor")
public interface FeignControllerClient {

    @PostMapping(value = "/offers")
    List<LoanOfferDTO> getLoanOffers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping(value = "/calculation")
    CreditDTO calculate(@RequestBody ScoringDataDTO scoringDataDTO);
}
