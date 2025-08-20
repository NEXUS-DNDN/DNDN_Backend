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
    @GetMapping("/services")
    public WelfareListResDto getAllWelfare() {
        return welfareService.welfareFindAll(1, 100); // paging 값은 예시
    }

    // 복지 상세 조회
    @GetMapping("/{welfare-id}")
    public WelfareDetailResDto getWelfareDetail(@PathVariable("welfare-id") Long welfareId) {
        return welfareService.welfareFindById(welfareId);
    }

    // 복지명 검색
    @GetMapping("/search")
    public WelfareListResDto getWelfareByTitle(@RequestParam String title) {
        return welfareService.welfareFindByTitle(title);
    }

    // 카테고리 검색
    @GetMapping("/category")
    public WelfareListResDto getByCategory(
            @RequestParam LifeCycle lifeCycle,
            @RequestParam(required = false) List<HouseholdType> householdTypes,
            @RequestParam(required = false) List<InterestTopic> interestTopics
    ) {
        return welfareService.welfareFindByCategory(lifeCycle, householdTypes, interestTopics);
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

