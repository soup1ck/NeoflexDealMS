package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import ru.neoflex.deal.data.dto.ApplicationStatusHistoryDTO;
import ru.neoflex.deal.data.jsonb.StatusHistoryJsonb;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface StatusHistoryMapper {

    StatusHistoryJsonb toJsonb(ApplicationStatusHistoryDTO applicationStatusHistoryDTO);

    default Timestamp map(LocalDateTime localDateTime) {
        return localDateTime == null ? null : Timestamp.valueOf(localDateTime);
    }
}
