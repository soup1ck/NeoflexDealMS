package ru.neoflex.deal.data.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.neoflex.deal.data.enums.Theme;

@Getter
@Setter
@Builder
@ToString
public class EmailMessageDTO {

    private String address;
    private Theme theme;
    private Long applicationId;
}
