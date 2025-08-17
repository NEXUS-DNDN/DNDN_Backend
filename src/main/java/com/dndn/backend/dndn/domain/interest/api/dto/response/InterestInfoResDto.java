package com.dndn.backend.dndn.domain.interest.api.dto.response;

import com.dndn.backend.dndn.domain.category.domain.Category;
import com.dndn.backend.dndn.domain.interest.domain.Interest;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record InterestInfoResDto(
        Long welfareId,
        Boolean interestStatus,
        String welfareTitle,
        List<String> interestTopicNames,
        String department,
        String org,
        LocalDateTime endDate
) {
    public static InterestInfoResDto from(Interest interest) {
        return InterestInfoResDto.builder()
                .welfareId(interest.getWelfare().getId())
                .welfareTitle(interest.getWelfare().getTitle())
                .interestStatus(interest.getInterestStatus())
                .interestTopicNames(interest.getWelfare().getCategory().getInterestTopics().stream()
                        .map(it -> it.getKor())
                        .collect(Collectors.toList()))
                .department(interest.getWelfare().getDepartment()) // ↓ ※ Welfare에 필드가 있어야 합니다
                .org(interest.getWelfare().getOrg())
                .endDate(interest.getWelfare().getEndDate())
                .build();
    }
}
