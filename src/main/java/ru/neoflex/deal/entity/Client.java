package ru.neoflex.deal.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import ru.neoflex.deal.data.jsonb.EmploymentJsonb;
import ru.neoflex.deal.data.jsonb.PassportJsonb;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "client_seq")
    @SequenceGenerator(name = "client_seq",
            sequenceName = "client_seq", allocationSize = 1)
    @Column(name = "client_id")
    private Long clientID;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @Column(name = "passport")
    @Type(JsonBinaryType.class)
    private PassportJsonb passport;

    @Column(name = "employment")
    @Type(JsonBinaryType.class)
    private EmploymentJsonb employment;

    @Column(name = "account")
    private String account;
}
