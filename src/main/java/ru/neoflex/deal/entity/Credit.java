package ru.neoflex.deal.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import ru.neoflex.deal.data.jsonb.PaymentScheduleJsonb;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "credit_seq")
    @SequenceGenerator(name = "credit_seq",
            sequenceName = "credit_seq", allocationSize = 1)
    @Column(name = "credit_id")
    private Long creditId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "term")
    private Integer term;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "psk")
    private BigDecimal psk;

    @Column(name = "payment_schedule")
    @Type(JsonBinaryType.class)
    private List<PaymentScheduleJsonb> paymentSchedule;

    @Column(name = "insurance_enable")
    private Boolean insuranceEnable;

    @Column(name = "salary_client")
    private Boolean salaryClient;

    @Column(name = "credit_status")
    private String creditStatus;
}
