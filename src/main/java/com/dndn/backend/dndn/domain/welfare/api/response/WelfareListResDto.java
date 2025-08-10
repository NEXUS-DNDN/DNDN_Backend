package com.dndn.backend.dndn.domain.welfare.api.response;

import lombok.Builder;

import java.util.List;

@Builder
public record WelfareListResDto(
        List<WelfareInfoResDto> welfareList
) {
    public static WelfareListResDto from(List<WelfareInfoResDto> welfareList) {
        return WelfareListResDto.builder()
                .welfareList(welfareList)
                .build();
    }
}
