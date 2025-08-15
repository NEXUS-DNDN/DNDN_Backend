package com.dndn.backend.dndn.domain.interest.api;

import com.dndn.backend.dndn.domain.interest.api.dto.response.InterestInfoResDto;
import com.dndn.backend.dndn.domain.interest.api.dto.response.InterestListResDto;
import com.dndn.backend.dndn.domain.interest.application.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/interest")
public class InterestController {
    private final InterestService interestService;

    // 관심 생성 및 삭제
    @PatchMapping("/{welfareId}")
    public ResponseEntity<InterestInfoResDto> updateInterest(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @PathVariable Long welfareId,
            @RequestParam("interestStatus") boolean interestStatus
    ) {
        InterestInfoResDto res = interestService.updateInterest(userId, welfareId, interestStatus);
        return ResponseEntity.ok(res);
    }

    // 사용자 관심 목록 조회
    @GetMapping
    public ResponseEntity<InterestListResDto> getInterests(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestParam(value = "interestStatus", required = false) String statusParam
    ) {
        Boolean status = parseStatus(statusParam);
        // 파라미터가 없었다면 기본 true로
        if (statusParam == null) status = Boolean.TRUE;
        return ResponseEntity.ok(interestService.getInterest(userId, status));
    }


    private Boolean parseStatus(String raw) {
        if (raw == null) return null;
        String v = raw.trim().toLowerCase();
        return switch (v) {
            case "true" -> Boolean.TRUE;
            case "false" -> Boolean.FALSE;
            case "all", "" -> null; // 전체
            default -> throw new IllegalArgumentException("interestStatus는 [true,false,all] 값 중 하나여야 합니다.");
        };
    }
}
