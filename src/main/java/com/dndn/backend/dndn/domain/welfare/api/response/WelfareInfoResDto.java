package com.dndn.backend.dndn.domain.welfare.api.response;

import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import lombok.Builder;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record WelfareInfoResDto(
        Long welfareId,
        String title,
        String summary,
        List<String> lifeCycleNames,
        List<String> householdTypeNames,
        List<String> interestTopicNames
) {
    public static WelfareInfoResDto from(Welfare welfare) {
        return WelfareInfoResDto.builder()
                .welfareId(welfare.getId())
                .title(welfare.getTitle())
                .summary(welfare.getSummary())
                .lifeCycleNames(welfare.getCategory().getLifeCycles().stream()
                        .map(lc -> lc.getKor())
                        .collect(Collectors.toList()))
                .householdTypeNames(welfare.getCategory().getHouseholdTypes().stream()
                        .map(ht -> ht.getKor())
                        .collect(Collectors.toList()))
                .interestTopicNames(welfare.getCategory().getInterestTopics().stream()
                        .map(it -> it.getKor())
                        .collect(Collectors.toList()))
                .build();
    }
}
