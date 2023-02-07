package ru.neoflex.deal.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import ru.neoflex.deal.data.jsonb.LoanOfferJsonb;
import ru.neoflex.deal.data.jsonb.StatusHistoryJsonb;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "application_seq")
    @SequenceGenerator(name = "application_seq",
            sequenceName = "application_seq", allocationSize = 1)
    @Column(name = "application_id")
    private Long applicationId;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @Column(name = "status")
    private String status;

    @CreationTimestamp
    @Column(name = "creation_date", updatable = false)
    private Timestamp creationDate;

    @Column(name = "applied_offer")
    @Type(JsonBinaryType.class)
    private LoanOfferJsonb appliedOffer;

    @Column(name = "sign_date")
    private Timestamp signDate;

    @Column(name = "ses_code")
    private Integer sesCode;

    @Column(name = "status_history")
    @Type(JsonBinaryType.class)
    private List<StatusHistoryJsonb> statusHistory;
}
