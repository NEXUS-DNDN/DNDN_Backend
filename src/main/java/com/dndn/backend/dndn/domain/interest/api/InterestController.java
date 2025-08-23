package com.dndn.backend.dndn.domain.interest.api;

import com.dndn.backend.dndn.domain.interest.api.dto.response.InterestInfoResDto;
import com.dndn.backend.dndn.domain.interest.api.dto.response.InterestListResDto;
import com.dndn.backend.dndn.domain.interest.application.InterestService;
import com.dndn.backend.dndn.domain.interest.exception.InterestException;
import com.dndn.backend.dndn.global.common.response.BaseResponse;
import com.dndn.backend.dndn.global.error.code.status.ErrorStatus;
import com.dndn.backend.dndn.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/interest")
public class InterestController {
    private final InterestService interestService;

    // 관심 생성 및 삭제
    @PatchMapping("/{welfareId}")
    @Operation(
            summary = "관심 등록 및 변경",
            description = """
                    interestStatus에 따라
                    - true면 관심 등록
                    - false면 관심 해제
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "INTEREST_200", description = "관심 상태가 성공적으로 업데이트되었습니다."),
            @ApiResponse(responseCode = "USER4001", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "WELFARE4001", description = "존재하지 않는 복지 서비스입니다."),
            @ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다.")
    })
    public BaseResponse<InterestInfoResDto> updateInterest(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @Parameter(description = "대상 복지 서비스 ID")
            @PathVariable Long welfareId,
            @Parameter(description = "관심 상태(true: 등록, false: 해제)")
            @RequestParam("interestStatus") boolean interestStatus
    ) {
        InterestInfoResDto res = interestService.updateInterest(userId, welfareId, interestStatus);
        return BaseResponse.onSuccess(SuccessStatus.INTEREST_UPDATED, res);
    }

    // 사용자 관심 목록 조회
    @GetMapping
    @Operation(
            summary = "관심 목록 조회",
            description = """
                    사용자 관심 목록을 조회합니다.
                    - interestStatus 미지정 시 기본값은 true 입니다.
                    - true: 관심 등록된 항목만
                    - false: 관심 해제된 항목만
                    - all: 관심 등록/해제 전부
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON_200", description = "성공"),
            @ApiResponse(responseCode = "USER4001", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.")
    })
    public BaseResponse<InterestListResDto> getInterests(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @Parameter(description = "관심 상태 필터 (기본값 true)")
            @RequestParam(value = "interestStatus", required = false) String statusParam
    ) {
        Boolean status = parseStatus(statusParam);
        // 파라미터가 없었다면 기본 true로
        if (statusParam == null) status = Boolean.TRUE;
        var res = interestService.getInterest(userId, status);
        return BaseResponse.onSuccess(SuccessStatus.OK, res);
    }


    private Boolean parseStatus(String raw) {
        if (raw == null) return null;
        String v = raw.trim().toLowerCase();
        return switch (v) {
            case "true" -> Boolean.TRUE;
            case "false" -> Boolean.FALSE;
            case "all", "" -> null; // 전체
            default -> throw new InterestException(ErrorStatus.INTEREST_INVALID_STATUS);
        };
    }
}
