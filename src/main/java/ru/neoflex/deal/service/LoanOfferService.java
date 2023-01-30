package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.data.dto.LoanApplicationRequestDTO;
import ru.neoflex.deal.data.dto.LoanOfferDTO;
import ru.neoflex.deal.utils.FeignControllerClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanOfferService {

    private final FeignControllerClient feignControllerClient;

    public List<LoanOfferDTO> getUpdatedLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO,
                                                   Long applicationId) {
        log.info("Процесс обновления предложений начат");
        List<LoanOfferDTO> loanOffers = getLoanOffers(loanApplicationRequestDTO);
        for (LoanOfferDTO loanOffer : loanOffers) {
            loanOffer.setApplicationId(applicationId);
        }
        log.info("Процесс обновления предложений закончен, предложения: {}", loanOffers);
        return loanOffers;
    }

    private List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Процесс отправки запроса на МС КК для получения предложений начат");
        List<LoanOfferDTO> loanOffers = feignControllerClient.getLoanOffers(loanApplicationRequestDTO);
        log.info("Процесс отправки запроса на МС КК для получения предложений закончен, предложения: {}",
                loanOffers);
        return loanOffers;
    }
}
