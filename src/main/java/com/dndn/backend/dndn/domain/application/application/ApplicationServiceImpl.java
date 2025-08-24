package com.dndn.backend.dndn.domain.application.application;

import com.dndn.backend.dndn.domain.application.api.dto.request.ApplicationCreateReqDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationCreateResDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationInfoResDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationListResDto;
import com.dndn.backend.dndn.domain.application.domain.Application;
import com.dndn.backend.dndn.domain.application.domain.enums.ReceiveStatus;
import com.dndn.backend.dndn.domain.application.domain.repository.ApplicationRepository;
import com.dndn.backend.dndn.domain.application.exception.ApplicationException;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import com.dndn.backend.dndn.domain.user.domain.repository.UserRepository;
import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import com.dndn.backend.dndn.domain.welfare.domain.repository.WelfareRepository;
import com.dndn.backend.dndn.global.error.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final WelfareRepository welfareRepository;
    private final UserRepository userRepository;

    // 복지 서비스 신청
    @Override
    @Transactional
    public ApplicationCreateResDto createApplication(Long welfareId, Long userId, ApplicationCreateReqDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus._USER_NOT_FOUND));

        Welfare welfare = welfareRepository.findById(welfareId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus._WELFARE_NOT_FOUND));

        // 중복 신청 방지
        if (applicationRepository.existsByUser_IdAndWelfare_Id(userId, welfareId)) {
            throw new ApplicationException(ErrorStatus.APPLICATION_DUPLICATED);
        }

        Application application = Application.builder()
                .user(user)
                .welfare(welfare)
                .appliedAt(request.appliedAt())
                .build();

        Application saved = applicationRepository.save(application);
        return ApplicationCreateResDto.of(saved);
    }

    // 신청/혜택 수령 완료 목록 조회
    @Override
    @Transactional(readOnly = true)
    public ApplicationListResDto getApplications(Long userId, ReceiveStatus status) {
        if (!userRepository.existsById(userId)) {
            throw new ApplicationException(ErrorStatus._USER_NOT_FOUND);
        }

        List<Application> applications = applicationRepository.findAllWithWelfareByUserAndStatus(userId, status);

        List<ApplicationInfoResDto> applicationInfoResDtoList = applications.stream()
                .map(ApplicationInfoResDto::of)
                .toList();

        return ApplicationListResDto.from(applicationInfoResDtoList);
    }

    // 혜택 수령 완료 처리
    @Override
    @Transactional
    public void updateReceived(Long applicationId, Long userId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.APPLICATION_NOT_FOUND));

        if (!application.getUser().getId().equals(userId)) {
            throw new ApplicationException(ErrorStatus.APPLICATION_FORBIDDEN);
        }

        application.updateReceiveStatus();
    }

    // 신청/혜택 내역 삭제
    @Override
    @Transactional
    public void deleteReceived(Long applicationId, Long userId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.APPLICATION_NOT_FOUND));

        if (!application.getUser().getId().equals(userId)) {
            throw new ApplicationException(ErrorStatus.APPLICATION_FORBIDDEN);
        }

        // 수령 완료(RECEIVED) 상태인지 확인
        if (application.getReceiveStatus() != ReceiveStatus.RECEIVED) {
            throw new ApplicationException(ErrorStatus.APPLICATION_DELETE_NOT_ALLOWED);
        }

        applicationRepository.delete(application);
    }

}
