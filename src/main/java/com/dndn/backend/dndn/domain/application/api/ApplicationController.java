package com.dndn.backend.dndn.domain.application.api;

import com.dndn.backend.dndn.domain.application.api.dto.request.ApplicationCreateReqDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationCreateResDto;
import com.dndn.backend.dndn.domain.application.api.dto.response.ApplicationListResDto;
import com.dndn.backend.dndn.domain.application.application.ApplicationService;
import com.dndn.backend.dndn.domain.application.domain.enums.ReceiveStatus;
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
    public ResponseEntity<Void> markAsReceived(
            @PathVariable Long applicationId,
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        applicationService.updateReceived(applicationId, userId);
        return ResponseEntity.noContent().build();
    }


}
