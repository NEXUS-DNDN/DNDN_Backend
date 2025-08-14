package com.dndn.backend.dndn.domain.application.api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ApplicationListResDto(
        List<ApplicationInfoResDto> applicationList
) {
    public static ApplicationListResDto from(List<ApplicationInfoResDto> applicationList) {
        return ApplicationListResDto.builder()
                .applicationList(applicationList)
                .build();
    }
}
