package com.dndn.backend.dndn.domain.user.dto;

import com.dndn.backend.dndn.domain.model.enums.DisabilityType;
import com.dndn.backend.dndn.domain.user.domain.entity.Disabled;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DisabledResponseDTO {

    private int disabillityGrade;

    private DisabilityType disabilityType;

    private boolean registeredDisabled;


    public static DisabledResponseDTO from(Disabled disabled) {
        return DisabledResponseDTO.builder()
                .disabillityGrade(disabled.getDisabillityGrade())
                .disabilityType(disabled.getDisabilityType())
                .registeredDisabled(disabled.isRegisteredDisabled())
                .build();
    }
}
