package org.cook.kafkaforcook.dto;

import lombok.Data;
import org.cook.kafkaforcook.entity.CookEntity;

import java.time.LocalTime;

@Data
public class CookToProcessOrderDTO {
    CookEntity cook;
    LocalTime estimatedCompletionTime;

    public CookToProcessOrderDTO parseDTO(CookEntity cookToParse, LocalTime time) {
        CookToProcessOrderDTO dto = new CookToProcessOrderDTO();
        dto.setCook(cookToParse);
        dto.setEstimatedCompletionTime(time);
        return dto;
    }
}
