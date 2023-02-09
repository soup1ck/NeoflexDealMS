package ru.neoflex.deal.data.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.neoflex.deal.data.enums.ApplicationStatus;
import ru.neoflex.deal.data.enums.ChangeType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class ApplicationStatusHistoryDTO {

    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}
