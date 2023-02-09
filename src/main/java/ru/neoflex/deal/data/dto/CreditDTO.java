package ru.neoflex.deal.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class CreditDTO {

    @Schema(description = "Конечная сумма")
    private BigDecimal amount;

    @Schema(description = "Кол-во месяцев")
    private Integer term;

    @Schema(description = "Ежемесячный платеж")
    private BigDecimal monthlyPayment;

    @Schema(description = "Конечная ставка")
    private BigDecimal rate;

    @Schema(description = "Полная стоимость кредита")
    private BigDecimal psk;

    @Schema(description = "Включена ли страховка")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Зарплатный клиент")
    private Boolean isSalaryClient;

    @Schema(description = "Расписание платежей")
    private List<PaymentScheduleElement> paymentSchedule;
}
