package com.dndn.backend.dndn.domain.welfare.api.response;

import com.dndn.backend.dndn.domain.welfare.domain.enums.SourceType;
import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record WelfareDetailResDto(
        Long welfareId,
        String title,
        String summary,
        String content,
        List<String> lifeCycleNames,
        List<String> householdTypeNames,
        List<String> interestTopicNames,
        String servLink,
        String ctpvNm,
        String sggNm,
        String eligibleUser,
        String detailInfo,
        String department,
        String org,
        SourceType sourceType
) {
    public static WelfareDetailResDto of(Welfare welfare) {
        return WelfareDetailResDto.builder()
                .welfareId(welfare.getId())
                .title(welfare.getTitle())
                .summary(welfare.getSummary())
                .content(welfare.getContent())
                .lifeCycleNames(welfare.getCategory().getLifeCycles().stream()
                        .map(lc -> lc.getKor())
                        .collect(Collectors.toList()))
                .householdTypeNames(welfare.getCategory().getHouseholdTypes().stream()
                        .map(ht -> ht.getKor())
                        .collect(Collectors.toList()))
                .interestTopicNames(welfare.getCategory().getInterestTopics().stream()
                        .map(it -> it.getKor())
                        .collect(Collectors.toList()))
                .servLink(welfare.getServLink())
                .ctpvNm(welfare.getCtpvNm())
                .sggNm(welfare.getSggNm())
                .department(welfare.getDepartment())
                .org(welfare.getOrg())
                .eligibleUser(welfare.getEligibleUser())
                .detailInfo(welfare.getDetailInfo())
                .sourceType(welfare.getSourceType())
                .build();
    }
}

