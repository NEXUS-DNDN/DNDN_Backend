package com.dndn.backend.dndn.domain.application.application;

import com.dndn.backend.dndn.domain.application.api.dto.request.ApplicationCreateReqDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationCreateResDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationInfoResDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationListResDto;
import com.dndn.backend.dndn.domain.application.domain.enums.ReceiveStatus;

import java.util.List;

public interface ApplicationService {

    // 복지 서비스 신청
    ApplicationCreateResDto createApplication(
            Long welfareId, Long userId,
            ApplicationCreateReqDto request);

    // 신청/혜택 수령 목록 조회
    ApplicationListResDto getApplications(
            Long userId, ReceiveStatus status);

    // 혜택 수령 완료 처리
    void updateReceived(Long applicationId, Long userId);

    // 혜택 내역 삭제
    void deleteReceived(Long applicationId, Long userId);

    // 수령상태 수령 전으로 되돌리기
    void revertReceived(Long applicationId, Long userId);

}
