package com.dndn.backend.dndn.domain.application.api.dto.response;

import com.dndn.backend.dndn.domain.application.domain.Application;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ApplicationCreateResDto(
        Long applicationId,
        Long welfareId,
        String title,
        LocalDate appliedAt
) {
    public static ApplicationCreateResDto of(Application application) {
        return ApplicationCreateResDto.builder()
                .applicationId(application.getId())
                .welfareId(application.getWelfare().getId())
                .title(application.getWelfare().getTitle())
                .appliedAt(application.getAppliedAt())
                .build();
    }
}
