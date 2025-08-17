package com.dndn.backend.dndn.domain.application.application;

import com.dndn.backend.dndn.domain.application.api.dto.request.ApplicationCreateReqDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationCreateResDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationInfoResDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationListResDto;
import com.dndn.backend.dndn.domain.application.domain.Application;
import com.dndn.backend.dndn.domain.application.domain.enums.ReceiveStatus;
import com.dndn.backend.dndn.domain.application.domain.repository.ApplicationRepository;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import com.dndn.backend.dndn.domain.user.domain.repository.UserRepository;
import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import com.dndn.backend.dndn.domain.welfare.domain.repository.WelfareRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. userId=" + userId));

        Welfare welfare = welfareRepository.findById(welfareId)
                .orElseThrow(() -> new IllegalArgumentException("복지 정보를 찾을 수 없습니다. welfareId=" + welfareId));

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
        List<Application> applications = applicationRepository.findAllWithWelfareByUserAndStatus(userId, status);

        List<ApplicationInfoResDto> applicationInfoResDtoList = applications.stream()
                .map(ApplicationInfoResDto::of)
                .toList();

        return ApplicationListResDto.from(applicationInfoResDtoList);
    }

    @Override
    @Transactional
    public void updateReceived(Long applicationId, Long userId) {
        Application application = applicationRepository.findByIdAndUser_Id(applicationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("신청 내역이 없거나 소유자가 아닙니다. applicationId=" + applicationId));

        application.updateReceiveStatus();
    }

}
