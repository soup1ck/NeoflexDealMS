package ru.neoflex.deal.data.dto;

import lombok.Getter;
import lombok.Setter;
import ru.neoflex.deal.data.enums.ApplicationStatus;
import ru.neoflex.deal.data.enums.ChangeType;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApplicationStatusHistoryDTO {

      private ApplicationStatus status;
      private LocalDateTime time;
      private ChangeType changeType;
}
