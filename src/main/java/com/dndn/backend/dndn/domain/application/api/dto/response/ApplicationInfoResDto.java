package com.dndn.backend.dndn.domain.application.api.dto.response;

import com.dndn.backend.dndn.domain.application.domain.Application;
import com.dndn.backend.dndn.domain.application.domain.enums.ReceiveStatus;
import com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ApplicationInfoResDto(
        Long applicationId,
        Long welfareId,
        String title,
        String department,  // 담당부처
        String org,         // 담당기관명
        LocalDate appliedAt,
        ReceiveStatus receiveStatus,
        String servLink,
        List<LifeCycle> lifeCycles,
        String summary
) {
    public static ApplicationInfoResDto of(Application application) {
        return ApplicationInfoResDto.builder()
                .applicationId(application.getId())
                .welfareId(application.getWelfare().getId())
                .title(application.getWelfare().getTitle())
                .department(application.getWelfare().getDepartment())
                .org(application.getWelfare().getOrg())
                .appliedAt(application.getAppliedAt())
                .receiveStatus(application.getReceiveStatus())
                .servLink(application.getWelfare().getServLink())
                .lifeCycles(application.getWelfare().getCategory().getLifeCycles())
                .summary(application.getWelfare().getSummary())
                .build();
    }
}
