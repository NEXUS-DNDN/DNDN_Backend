package com.dndn.backend.dndn.domain.welfare.api;

import com.dndn.backend.dndn.domain.category.domain.enums.HouseholdType;
import com.dndn.backend.dndn.domain.category.domain.enums.InterestTopic;
import com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle;
import com.dndn.backend.dndn.domain.welfare.api.response.WelfareDetailResDto;
import com.dndn.backend.dndn.domain.welfare.api.response.WelfareListResDto;
import com.dndn.backend.dndn.domain.welfare.application.WelfareService;
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

    // 전체 복지 목록 조회
    @GetMapping("/list")
    @Operation(
            summary = "전체 복지 서비스 목록 조회",
            description = "전체 복지 서비스를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "목록 조회 성공")
    })
    public WelfareListResDto getAllWelfare() {
        return welfareService.welfareFindAll(1, 100); // paging 값은 예시
    }

    // 복지 상세 조회
    @GetMapping("/{welfare-id}")
    @Operation(
            summary = "복지 서비스 상세 조회",
            description = "복지 서비스 ID로 상세 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상세 조회 성공")
    })
    public WelfareDetailResDto getWelfareDetail(@PathVariable("welfare-id") Long welfareId) {
        return welfareService.welfareFindById(welfareId);
    }

    // 복지명 검색
    @GetMapping("/search")
    @Operation(
            summary = "복지 서비스 검색",
            description = "복지 서비스 이름(제목)에 포함된 키워드로 검색합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공")
    })
    public WelfareListResDto getWelfareByTitle(@RequestParam String title) {
        return welfareService.welfareFindByTitle(title);
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
            @ApiResponse(responseCode = "200", description = "검색 성공")
    })
    public WelfareListResDto getByCategory(
            @RequestParam LifeCycle lifeCycle,
            @RequestParam(required = false) List<HouseholdType> householdTypes,
            @RequestParam(required = false) List<InterestTopic> interestTopics
    ) {
        return welfareService.welfareFindByCategory(lifeCycle, householdTypes, interestTopics);
    }
}

