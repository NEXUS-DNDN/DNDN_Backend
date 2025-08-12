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
    public ResponseEntity<InterestListResDto> interestsFind(
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        InterestListResDto res = interestService.getInterest(userId);
        return ResponseEntity.ok(res);
    }
}
