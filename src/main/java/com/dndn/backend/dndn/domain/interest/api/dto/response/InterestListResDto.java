package com.dndn.backend.dndn.domain.interest.api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record InterestListResDto(
        List<InterestInfoResDto> interestList
) {
    public static InterestListResDto from(List<InterestInfoResDto> interestList) {
        return InterestListResDto.builder()
                .interestList(interestList)
                .build();
    }
}
