package ru.neoflex.deal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping(value = "/offer")
    public void updateApplication(@RequestBody LoanOfferDTO loanOfferDTO){
        dealService.updateApplication(loanOfferDTO);
    }

    @PutMapping(value = "/calculate/{applicationId}")
    public void calculateRequest(@PathVariable Long applicationId){
    }
}
