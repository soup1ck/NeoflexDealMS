package ru.neoflex.deal.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.neoflex.deal.data.dto.LoanApplicationRequestDTO;
import ru.neoflex.deal.data.dto.LoanOfferDTO;
import ru.neoflex.deal.utils.FeignControllerClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanOfferServiceTest {

    private final FeignControllerClient feignControllerClient =
            Mockito.mock(FeignControllerClient.class);
    private final LoanOfferService loanOfferService = new LoanOfferService(feignControllerClient);

    @Test
    @DisplayName(value = "Проверка создания предложений с applicationId")
    void getUpdatedLoanOffers() {

        List<LoanOfferDTO> loanOffers = new ArrayList<>();
        loanOffers.add(new LoanOfferDTO());
        loanOffers.add(new LoanOfferDTO());
        loanOffers.add(new LoanOfferDTO());
        loanOffers.add(new LoanOfferDTO());

        Mockito.when(feignControllerClient.getLoanOffers(Mockito.any(LoanApplicationRequestDTO.class)))
                .thenReturn(loanOffers);

        List<LoanOfferDTO> updatedLoanOffers = loanOfferService.getUpdatedLoanOffers(new LoanApplicationRequestDTO(), 1L);

        Assertions.assertEquals(1L, updatedLoanOffers.get(0).getApplicationId());
    }
}