package ru.neoflex.deal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.deal.data.dto.LoanApplicationRequestDTO;
import ru.neoflex.deal.data.dto.LoanOfferDTO;
import ru.neoflex.deal.service.DealService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/deal")
public class DealController {

    private final DealService dealService;

    @PostMapping(value = "/application")
    public List<LoanOfferDTO> insertClient(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return dealService.handleLoanRequest(loanApplicationRequestDTO);
    }
}
