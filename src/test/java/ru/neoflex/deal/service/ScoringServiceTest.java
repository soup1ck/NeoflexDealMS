package ru.neoflex.deal.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.neoflex.deal.data.dto.FinishRegistrationRequestDTO;
import ru.neoflex.deal.data.dto.ScoringDataDTO;
import ru.neoflex.deal.data.enums.Gender;
import ru.neoflex.deal.data.jsonb.LoanOfferJsonb;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.mapper.FinishRegistrationRequestMapper;

import java.math.BigDecimal;

class ScoringServiceTest {

    private final FinishRegistrationRequestMapper finishRegistrationRequestMapper =
            Mockito.mock(FinishRegistrationRequestMapper.class);
    private final ScoringService scoringService = new ScoringService(finishRegistrationRequestMapper);

    private LoanOfferJsonb loanOfferJsonb;
    private FinishRegistrationRequestDTO finishRegistrationRequestDTO;
    private Client client;

    @BeforeEach
    public void setUp() {
        loanOfferJsonb = new LoanOfferJsonb();
        loanOfferJsonb.setTotalAmount(new BigDecimal("10"));
        loanOfferJsonb.setTerm(6);
        loanOfferJsonb.setIsInsuranceEnabled(true);
        loanOfferJsonb.setIsSalaryClient(true);
        finishRegistrationRequestDTO = new FinishRegistrationRequestDTO();
        finishRegistrationRequestDTO.setGender(Gender.MALE);
        client = new Client();
        client.setFirstName("test");
    }

    @Test
    @DisplayName(value = "Создание scoringData")
    void createScoringData() {

        Mockito.when(finishRegistrationRequestMapper.toScoringData(finishRegistrationRequestDTO, client))
                .thenAnswer(invocationOnMock -> {
                    ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
                    scoringDataDTO.setFirstName("test");
                    scoringDataDTO.setIsSalaryClient(true);
                    scoringDataDTO.setIsSalaryClient(true);
                    return scoringDataDTO;
                });

        ScoringDataDTO scoringData = scoringService.createScoringData(loanOfferJsonb,
                finishRegistrationRequestDTO, client);

        Assertions.assertEquals("test", scoringData.getFirstName());
        Assertions.assertEquals(true, scoringData.getIsSalaryClient());
        Assertions.assertEquals(new BigDecimal("10"), scoringData.getAmount());
    }
}