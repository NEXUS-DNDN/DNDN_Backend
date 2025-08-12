package com.dndn.backend.dndn.domain.interest.application;

import com.dndn.backend.dndn.domain.interest.api.dto.response.InterestInfoResDto;
import com.dndn.backend.dndn.domain.interest.api.dto.response.InterestListResDto;

public interface InterestService {
    // 관심 생성/삭제
    InterestInfoResDto updateInterest(Long userId, Long welfareId, boolean interestStatus);

    // 관심 목록 조회(ON만)
    InterestListResDto getInterest(Long userId);
}
