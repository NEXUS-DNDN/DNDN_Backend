package com.dndn.backend.dndn.domain.application.api.dto.response;

import com.dndn.backend.dndn.domain.application.domain.Application;
import com.dndn.backend.dndn.domain.application.domain.enums.ReceiveStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record ApplicationInfoResDto(
        Long applicationId,
        Long welfareId,
        String title,
        String imageUrl,
        String department,  // 담당부처
        String org,         // 담당기관명
        LocalDate appliedAt,
        LocalDateTime endDate,
        ReceiveStatus receiveStatus,
        String servLink
) {
    public static ApplicationInfoResDto of(Application application) {
        return ApplicationInfoResDto.builder()
                .applicationId(application.getId())
                .welfareId(application.getWelfare().getId())
                .title(application.getWelfare().getTitle())
                .imageUrl(application.getWelfare().getImageUrl()) // Welfare 엔티티 필드명에 맞게 수정
                .department(application.getWelfare().getDepartment())
                .org(application.getWelfare().getOrg())
                .appliedAt(application.getAppliedAt())
                .endDate(application.getWelfare().getEndDate())
                .receiveStatus(application.getReceiveStatus())
                .servLink(application.getWelfare().getServLink())
                .build();
    }
}
