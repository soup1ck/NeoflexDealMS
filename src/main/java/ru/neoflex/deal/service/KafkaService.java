package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.data.dto.EmailMessageDTO;
import ru.neoflex.deal.data.enums.Theme;
import ru.neoflex.deal.entity.Application;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, EmailMessageDTO> template;
    private final ApplicationService applicationService;

    public void sendToFinishRegistrationTopic(Long applicationId) {
        Application application = applicationService.getApplicationByApplicationId(applicationId);
        String email = application.getClient().getEmail();
        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .address(email)
                .applicationId(applicationId)
                .theme(Theme.FINISH_REGISTRATION)
                .build();
        template.send("finish-registration", emailMessageDTO);
    }

    public void sendToCreateDocumentsTopic(Long applicationId) {
        Application application = applicationService.getApplicationByApplicationId(applicationId);
        String email = application.getClient().getEmail();
        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .address(email)
                .applicationId(applicationId)
                .theme(Theme.CREATE_DOCUMENT)
                .build();
        template.send("create-documents", emailMessageDTO);
    }

    public void sendToSendSesTopic(Long applicationId) {
        Application application = applicationService.getApplicationByApplicationId(applicationId);
        String email = application.getClient().getEmail();
        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .address(email)
                .applicationId(applicationId)
                .theme(Theme.SIGN_DOCUMENTS_WITH_SES_CODE)
                .build();
        template.send("send-ses", emailMessageDTO);
    }

    public void sendToSendDocumentsTopic(Long applicationId) {
        Application application = applicationService.getApplicationByApplicationId(applicationId);
        String email = application.getClient().getEmail();
        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .address(email)
                .applicationId(applicationId)
                .theme(Theme.YOUR_LOAN_DOCUMENTS)
                .build();
        template.send("send-documents", emailMessageDTO);
    }

    public void sendToCreditIssuedTopic(Long applicationId) {
        Application application = applicationService.getApplicationByApplicationId(applicationId);
        String email = application.getClient().getEmail();
        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .address(email)
                .applicationId(applicationId)
                .theme(Theme.CREDIT_ISSUED)
                .build();
        template.send("credit-issued", emailMessageDTO);
    }

    public void sendToApplicationDeniedTopic(Long applicationId) {
        Application application = applicationService.getApplicationByApplicationId(applicationId);
        String email = application.getClient().getEmail();
        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .address(email)
                .applicationId(applicationId)
                .theme(Theme.APPLICATION_DENIED)
                .build();
        template.send("application-denied", emailMessageDTO);
    }
}
