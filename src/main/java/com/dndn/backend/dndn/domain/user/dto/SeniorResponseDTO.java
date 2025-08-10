package com.dndn.backend.dndn.domain.user.dto;

import com.dndn.backend.dndn.domain.user.domain.entity.Senior;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeniorResponseDTO {

    private boolean livingWithChildren;

    private boolean houseHolder;

    public static SeniorResponseDTO from(Senior senior) {
        return SeniorResponseDTO.builder()
                .livingWithChildren(senior.isLivingWithChildren())
                .houseHolder(senior.isHouseHolder())
                .build();
    }
}
