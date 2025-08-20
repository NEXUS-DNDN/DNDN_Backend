package com.dndn.backend.dndn.domain.welfare.support;

import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

//추천용 복지 점수 계산 결과를 담는 내부 연산용 객체
public class WelfareWithScore {

    private final Welfare welfare;
    private final double score;

}
