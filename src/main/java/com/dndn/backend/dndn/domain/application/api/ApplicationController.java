package com.dndn.backend.dndn.domain.application.api;

import com.dndn.backend.dndn.domain.application.api.dto.request.ApplicationCreateReqDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationCreateResDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationListResDto;
import com.dndn.backend.dndn.domain.application.application.ApplicationService;
import com.dndn.backend.dndn.domain.application.domain.enums.ReceiveStatus;
import com.dndn.backend.dndn.global.common.response.BaseResponse;
import com.dndn.backend.dndn.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationController {
    private final ApplicationService applicationService;

    // 복지 서비스 신청
    @PostMapping("/{welfare-id}")
    @Operation(
            summary = "복지 서비스 신청",
            description = """
                    사용자가 특정 복지 서비스 신청을 생성합니다.
                    신청 날짜(appliedAt)를 요청 바디에 포함해야 합니다.
                    """
    )
    @ApiResponses(value = {
            // 성공
            @ApiResponse(responseCode = "APPLICATION_201", description = "신청이 완료 되었습니다."),
            // 표준 HTTP 상태 기반 에러들 (도메인/공통)
            @ApiResponse(responseCode = "USER4001", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "WELFARE4001", description = "존재하지 않는 복지 서비스입니다."),
            @ApiResponse(responseCode = "APPLICATION4091", description = "이미 신청된 복지입니다."),
            @ApiResponse(responseCode = "COMMON401", description = "인증이 필요합니다.")
    })
    public BaseResponse<ApplicationCreateResDto> createApplication(
            @PathVariable("welfare-id") Long welfareId,
            @Valid @RequestBody ApplicationCreateReqDto request,
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        ApplicationCreateResDto res = applicationService.createApplication(welfareId, userId, request);
        return BaseResponse.onSuccess(SuccessStatus.APPLICATION_CREATED, res);
    }

    // 신청/혜택 수령 완료 목록 조회
    @GetMapping("/list")
    @Operation(
            summary = "신청/수령 목록 조회",
            description = """
                    사용자의 복지 서비스 신청 내역을 조회합니다.
                    - tab=applied (기본값): 신청 완료, 수령 전
                    - tab=received: 혜택 수령 완료
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON_200", description = "성공입니다."),
            @ApiResponse(responseCode = "USER4001", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "COMMON401", description = "인증이 필요합니다.")
    })
    public BaseResponse<ApplicationListResDto> getApplications(
            @RequestParam(name = "tab", defaultValue = "applied") String tab,
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        ReceiveStatus status = "received".equalsIgnoreCase(tab)
                ? ReceiveStatus.RECEIVED
                : ReceiveStatus.NOT_RECEIVED;

        ApplicationListResDto res = applicationService.getApplications(userId, status);
        return BaseResponse.onSuccess(SuccessStatus.OK, res);
    }

    // 혜택 수령 완료 처리
    @PatchMapping("/applications/{applicationId}/receive")
    @Operation(
            summary = "혜택 수령 완료 처리",
            description = """
                    사용자가 신청한 복지 서비스의 혜택 수령을 완료 상태로 변경합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "APPLICATION_204", description = "수령 상태 업데이트 성공"),
            @ApiResponse(responseCode = "APPLICATION4001", description = "존재하지 않는 신청 내역입니다."),
            @ApiResponse(responseCode = "APPLICATION4031", description = "해당 신청 내역에 접근 권한이 없습니다.")
    })
    public BaseResponse<Void> markAsReceived(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        applicationService.updateReceived(applicationId, userId);
        return BaseResponse.onSuccess(SuccessStatus.APPLICATION_RECEIVED_UPDATED, null);
    }


}
