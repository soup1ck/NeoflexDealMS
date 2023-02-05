package ru.neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.deal.data.dto.FinishRegistrationRequestDTO;
import ru.neoflex.deal.data.dto.LoanApplicationRequestDTO;
import ru.neoflex.deal.data.dto.LoanOfferDTO;
import ru.neoflex.deal.entity.Application;
import ru.neoflex.deal.service.DealService;
import ru.neoflex.deal.service.KafkaService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/deal")
@Tag(name = "Сделка",
        description = "Микросервис для работы с базой данных")
public class DealController {

    private final DealService dealService;
    private final KafkaService kafkaService;

    @Operation(summary = "Получение предложений по кредиту, занесение данных в таблицы")
    @PostMapping(value = "/application")
    public List<LoanOfferDTO> insertClient(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return dealService.getLoanOffers(loanApplicationRequestDTO);
    }

    @Operation(summary = "Установка принятого предложения")
    @PutMapping(value = "/offer")
    public void updateApplication(@RequestBody LoanOfferDTO loanOfferDTO) {
        dealService.updateApplication(loanOfferDTO);
    }

    @Operation(summary = "Насыщение ScoringDataDTO информацией и передача ее на МС КК")
    @PutMapping(value = "/calculate/{applicationId}")
    public void calculateRequest(@RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                 @PathVariable Long applicationId) {
        dealService.calculateCredit(finishRegistrationRequestDTO, applicationId);
    }

    @PostMapping(value = "/document/{applicationId}/send")
    public void sendRequestForDocument(@PathVariable Long applicationId) {
    }

    @PostMapping(value = "/document/{applicationId}/sign")
    public void sendRequestForSignDocument(@PathVariable Long applicationId) {
    }

    @PostMapping(value = "/document/{applicationId}/code")
    public void signDocument(@PathVariable Long applicationId) {
    }

    @GetMapping(value = "/admin/application/{applicationId}")
    public Application getApplicationById(@PathVariable Long applicationId) {
        return null;
    }

    @GetMapping(value = "/admin/application")
    public List<Application> getAllApplications() {
        return null;
    }
}
