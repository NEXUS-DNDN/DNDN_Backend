package com.dndn.backend.dndn.domain.interest.api.dto.response;

import com.dndn.backend.dndn.domain.interest.domain.Interest;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record InterestInfoResDto(
        Long welfareId,
        Boolean interestStatus,
        String welfareTitle,
        LocalDateTime endDate
) {
    public static InterestInfoResDto from(Interest interest) {
        return InterestInfoResDto.builder()
                .welfareId(interest.getWelfare().getId())
                .welfareTitle(interest.getWelfare().getTitle())
                .interestStatus(interest.getInterestStatus())
                .endDate(interest.getWelfare().getEndDate())
                .build();
    }
}
