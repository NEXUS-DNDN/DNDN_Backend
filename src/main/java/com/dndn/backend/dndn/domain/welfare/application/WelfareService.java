package com.dndn.backend.dndn.domain.welfare.application;

import com.dndn.backend.dndn.domain.welfare.api.response.WelfareDetailResDto;
import com.dndn.backend.dndn.domain.welfare.api.response.WelfareListResDto;

public interface WelfareService {

    // 복지 서비스 전체 목록 조회
    WelfareListResDto welfareFindAll(int page, int numOfRows);

    // 복지 서비스 상세 조회
    WelfareDetailResDto welfareFindById(Long servId);

    // 이름으로 복지 서비스 목록 조회
    WelfareListResDto welfareFindByTitle(String title);
}

