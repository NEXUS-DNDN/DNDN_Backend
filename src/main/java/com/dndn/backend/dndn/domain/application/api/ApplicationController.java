package com.dndn.backend.dndn.domain.application.api;

import com.dndn.backend.dndn.domain.application.api.dto.request.ApplicationCreateReqDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationCreateResDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationListResDto;
import com.dndn.backend.dndn.domain.application.application.ApplicationService;
import com.dndn.backend.dndn.domain.application.domain.enums.ReceiveStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @ApiResponse(responseCode = "201", description = "신청 성공")
    })
    public ResponseEntity<ApplicationCreateResDto> createApplication(
            @PathVariable("welfare-id") Long welfareId,
            @Valid @RequestBody ApplicationCreateReqDto request,
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        ApplicationCreateResDto res = applicationService.createApplication(welfareId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
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
            @ApiResponse(responseCode = "200", description = "목록 조회 성공")
    })
    public ResponseEntity<ApplicationListResDto> getApplications(
            @RequestParam(name = "tab", defaultValue = "applied") String tab,
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        ReceiveStatus status = "received".equalsIgnoreCase(tab)
                ? ReceiveStatus.RECEIVED
                : ReceiveStatus.NOT_RECEIVED;

        ApplicationListResDto res = applicationService.getApplications(userId, status);
        return ResponseEntity.ok(res);
    }

    // 혜택 수령 완료 처리
    @PatchMapping("/applications/{applicationId}/receive")
    @Operation(
            summary = "혜택 수령 완료 처리",
            description = """
                    사용자가 신청한 복지 서비스의 혜택 수령을 완료 상태로 변경합니다.
                    """
    )
    public ResponseEntity<Void> markAsReceived(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        applicationService.updateReceived(applicationId, userId);
        return ResponseEntity.noContent().build();
    }


}
