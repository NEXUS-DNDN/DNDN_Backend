package com.dndn.backend.dndn.domain.welfare.api;

import com.dndn.backend.dndn.domain.category.domain.enums.HouseholdType;
import com.dndn.backend.dndn.domain.category.domain.enums.InterestTopic;
import com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import com.dndn.backend.dndn.domain.user.service.UserService;
import com.dndn.backend.dndn.domain.welfare.api.response.WelfareDetailResDto;
import com.dndn.backend.dndn.domain.welfare.api.response.WelfareListResDto;
import com.dndn.backend.dndn.domain.welfare.application.WelfareService;
import com.dndn.backend.dndn.domain.welfare.dto.RecommendedWelfareResponseDTO;
import com.dndn.backend.dndn.global.common.response.BaseResponse;
import com.dndn.backend.dndn.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/welfare")
@RequiredArgsConstructor
public class WelfareController {

    private final WelfareService welfareService;
    private final UserService userService;

    // 전체 복지 목록 조회
    @GetMapping("/list")
    @Operation(
            summary = "전체 복지 서비스 목록 조회",
            description = "전체 복지 서비스를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON_200", description = "성공입니다."),
            @ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다.")
    })
    public BaseResponse<WelfareListResDto> getAllWelfare() {
        WelfareListResDto res = welfareService.welfareFindAll(1, 100);
        return BaseResponse.onSuccess(SuccessStatus.OK, res);
    }

    // 복지 상세 조회
    @GetMapping("/{welfare-id}")
    @Operation(
            summary = "복지 서비스 상세 조회",
            description = "복지 서비스 ID로 상세 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON_200", description = "성공입니다."),
            @ApiResponse(responseCode = "WELFARE4001", description = "존재하지 않는 복지 서비스입니다."),
            @ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다.")
    })
    public BaseResponse<WelfareDetailResDto> getWelfareDetail(@PathVariable("welfare-id") Long welfareId) {
        WelfareDetailResDto res = welfareService.welfareFindById(welfareId);
        return BaseResponse.onSuccess(SuccessStatus.OK, res);
    }

    // 복지명 검색
    @GetMapping("/search")
    @Operation(
            summary = "복지 서비스 검색",
            description = "복지 서비스 이름(제목)에 포함된 키워드로 검색합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON_200", description = "성공입니다."),
            @ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다.")
    })
    public BaseResponse<WelfareListResDto> getWelfareByTitle(@RequestParam String title) {
        WelfareListResDto res = welfareService.welfareFindByTitle(title);
        return BaseResponse.onSuccess(SuccessStatus.OK, res);
    }

    // 카테고리 검색
    @GetMapping("/category")
    @Operation(
            summary = "카테고리 기반 복지 서비스 조회",
            description = """
                    카테고리 조건으로 복지 서비스를 검색합니다.
                    - lifeCycle (필수)
                    - householdTypes (선택)
                    - interestTopics (선택)
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON_200", description = "성공입니다."),
            @ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다.")
    })
    public BaseResponse<WelfareListResDto> getByCategory(
            @RequestParam LifeCycle lifeCycle,
            @RequestParam(required = false) List<HouseholdType> householdTypes,
            @RequestParam(required = false) List<InterestTopic> interestTopics
    ) {
        WelfareListResDto res = welfareService.welfareFindByCategory(lifeCycle, householdTypes, interestTopics);
        return BaseResponse.onSuccess(SuccessStatus.OK, res);
    }

    @GetMapping("/recommendation")
    @Operation(summary = "추천 리스트 불러오기", description = "복지 추천 목록을 불러옵니다.")
    public BaseResponse<List<RecommendedWelfareResponseDTO>> recommendWelfare(
            @RequestParam("user-id") Long userId
    ) {
        User user = userService.getUserById(userId);

        List<RecommendedWelfareResponseDTO> response = welfareService.getRecommendedWelfares(user).stream()
                .map(RecommendedWelfareResponseDTO::from)
                .toList();

        return BaseResponse.onSuccess(
                SuccessStatus.OK,
                response
        );
    }

}

