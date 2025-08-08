package com.dndn.backend.dndn.domain.welfare.api.response;

import com.dndn.backend.dndn.domain.welfare.domain.enums.RequestStatus;
import com.dndn.backend.dndn.domain.welfare.domain.enums.ReceiveStatus;
import com.dndn.backend.dndn.domain.welfare.domain.enums.SourceType;
import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record WelfareDetailResDto(
        Long id,
        String title,
        String content,
        String imageUrl,
        List<String> lifeCycleNames,
        List<String> householdTypeNames,
        List<String> interestTopicNames,
        String servLink,
        String ctpvNm,
        String sggNm,
        String eligibleUser,
        String submitDocument,
        LocalDateTime startDate,
        LocalDateTime endDate,
        RequestStatus requestStatus,
        ReceiveStatus receiveStatus,
        SourceType sourceType
) {
    public static WelfareDetailResDto of(Welfare welfare) {
        return WelfareDetailResDto.builder()
                .id(welfare.getId())
                .title(welfare.getTitle())
                .content(welfare.getContent())
                .imageUrl(welfare.getImageUrl())
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
                .eligibleUser(welfare.getEligibleUser())
                .submitDocument(welfare.getSubmitDocument())
                .startDate(welfare.getStartDate())
                .endDate(welfare.getEndDate())
                .requestStatus(welfare.getRequestStatus())
                .receiveStatus(welfare.getReceiveStatus())
                .sourceType(welfare.getSourceType())
                .build();
    }
}

