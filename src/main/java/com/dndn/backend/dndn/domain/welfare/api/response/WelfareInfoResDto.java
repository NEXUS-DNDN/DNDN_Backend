package com.dndn.backend.dndn.domain.welfare.api.response;

import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

import static com.dndn.backend.dndn.domain.welfare.util.CategoryUtils.extractCategoryNames;

@Builder
public record WelfareInfoResDto(
        Long welfareId,
        String title,
        String imageUrl,
        List<String> categoryNames, // 상위 카테고리 이름
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    public static WelfareInfoResDto from(Welfare welfare) {
        return WelfareInfoResDto.builder()
                .welfareId(welfare.getId())
                .title(welfare.getTitle())
                .imageUrl(welfare.getImageUrl())
                .categoryNames(extractCategoryNames(welfare.getCategory()))
                .startDate(welfare.getStartDate())
                .endDate(welfare.getEndDate())
                .build();
    }
}
