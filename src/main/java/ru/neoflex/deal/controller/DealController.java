package ru.neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.deal.data.dto.FinishRegistrationRequestDTO;
import ru.neoflex.deal.data.dto.LoanApplicationRequestDTO;
import ru.neoflex.deal.data.dto.LoanOfferDTO;
import ru.neoflex.deal.data.enums.ApplicationStatus;
import ru.neoflex.deal.data.enums.ChangeType;
import ru.neoflex.deal.entity.Application;
import ru.neoflex.deal.service.ApplicationService;
import ru.neoflex.deal.service.DealService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/deal")
@Tag(name = "Сделка",
        description = "Микросервис для работы с базой данных")
public class DealController {

    private final DealService dealService;
    private final ApplicationService applicationService;

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

    @Operation(summary = "Запрос на отправку документов")
    @PostMapping(value = "/document/{applicationId}/send")
    public void sendRequestForDocument(@PathVariable Long applicationId) {
        dealService.sendRequestForDocument(applicationId);
    }

    @Operation(summary = "Запрос на подписание документов")
    @PostMapping(value = "/document/{applicationId}/sign")
    public void sendRequestForSignDocument(@PathVariable Long applicationId) {
        dealService.updateApplicationSesCode(applicationId);
    }

    @Operation(summary = "Подписание документов")
    @PostMapping(value = "/document/{applicationId}/{code}")
    public void signDocument(@PathVariable Long applicationId, @PathVariable Integer code) {
        dealService.verifySesCode(applicationId, code);
    }

    @Operation(summary = "Запрос на обновление статуса application")
    @PutMapping(value = "/admin/application/{applicationId}/status")
    public void updateApplicationStatus(@PathVariable Long applicationId) {
        dealService.updateApplicationStatus(applicationId, ApplicationStatus.DOCUMENT_CREATED,
                ChangeType.AUTOMATIC);
    }

    @Operation(summary = "Запрос на получение заявки по id ")
    @GetMapping(value = "/admin/application/{applicationId}")
    public Application getApplicationById(@PathVariable Long applicationId) {
        return applicationService.getApplicationByApplicationId(applicationId);
    }

    @Operation(summary = "Запрос на получение всех заявок")
    @GetMapping(value = "/admin/application")
    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }
}
