package com.dndn.backend.dndn.domain.welfare.application;

import com.dndn.backend.dndn.domain.category.domain.Category;
import com.dndn.backend.dndn.domain.category.domain.enums.HouseholdType;
import com.dndn.backend.dndn.domain.category.domain.enums.InterestTopic;
import com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import com.dndn.backend.dndn.domain.user.domain.repository.UserRepository;
import com.dndn.backend.dndn.domain.welfare.api.response.WelfareDetailResDto;
import com.dndn.backend.dndn.domain.welfare.api.response.WelfareListResDto;
import com.dndn.backend.dndn.domain.welfare.api.response.WelfareInfoResDto;
import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import com.dndn.backend.dndn.domain.welfare.domain.repository.WelfareRepository;
import com.dndn.backend.dndn.domain.welfare.exception.WelfareException;
import com.dndn.backend.dndn.domain.welfare.support.WelfareWithScore;
import com.dndn.backend.dndn.global.error.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WelfareServiceImpl implements WelfareService {

    private final WelfareRepository welfareRepository;
    private final UserRepository userRepository;

    // 복지 서비스 목록 전체 조회
    @Override
    public WelfareListResDto welfareFindAll(int page, int numOfRows) {
        List<Welfare> welfareList = welfareRepository.findAll();

        List<WelfareInfoResDto> welfareInfoResDtoList = welfareList.stream()
                .map(WelfareInfoResDto::from)
                .toList();
        return WelfareListResDto.from(welfareInfoResDtoList);
    }

    // 복지 id로 복지 서비스 상세 조회
    @Override
    public WelfareDetailResDto welfareFindById(Long welfareId) {
        Welfare welfare = welfareRepository.findById(welfareId)
                .orElseThrow(() -> new WelfareException(ErrorStatus._WELFARE_NOT_FOUND));
        return WelfareDetailResDto.of(welfare);
    }

    // 복지 이름으로 복지 서비스 목록 조회
    @Override
    public WelfareListResDto welfareFindByTitle(String title) {
        List<Welfare> welfareList = welfareRepository.findByTitleContaining(title);

        List<WelfareInfoResDto> welfareInfoResDtoList = welfareList.stream()
                .map(WelfareInfoResDto::from)
                .toList();
        return WelfareListResDto.from(welfareInfoResDtoList);
    }

    @Override
    public WelfareListResDto welfareFindByCategory(
            LifeCycle lifeCycle,
            List<HouseholdType> householdTypes,
            List<InterestTopic> interestTopics
    ) {
        // null-safe 처리
        List<HouseholdType> hh = (householdTypes == null) ? Collections.emptyList() : householdTypes;
        List<InterestTopic> it = (interestTopics == null) ? Collections.emptyList() : interestTopics;

        boolean householdsEmpty = hh.isEmpty();
        boolean interestsEmpty  = it.isEmpty();

        List<Welfare> result = welfareRepository.findByCategoryFilters(
                lifeCycle, hh, householdsEmpty, it, interestsEmpty
        );

        List<WelfareInfoResDto> dtoList = result.stream()
                .map(WelfareInfoResDto::from)
                .toList();

        return WelfareListResDto.from(dtoList);
    }

    @Override
    public List<Welfare> getRecommendedWelfares(User user) {
        List<Welfare> all = welfareRepository.findAll();

        return all.stream()
                .map(w -> new WelfareWithScore(w, calculateScore(user, w)))
                .filter(w -> w.getScore() > 0) // 지역 조건 불일치 등으로 점수 0이면 제외
                .sorted(Comparator.comparingDouble(WelfareWithScore::getScore).reversed())
                .map(WelfareWithScore::getWelfare)
                .toList();
    }

    private double calculateScore(User user, Welfare welfare) {
        if (!isRegionMatch(user.getAddress(), welfare.getCtpvNm(), welfare.getSggNm())) {
            return 0;
        }

        double score = 0;

        if (isLifeCycleMatched(user, welfare)) {
            score += 1;
        }

        score += countHouseholdTypeMatches(user, welfare);

        return score;
    }

    private boolean isLifeCycleMatched(User user, Welfare welfare) {
        LifeCycle userCycle = user.getLifeCycle();
        List<LifeCycle> targetCycles = welfare.getCategory().getLifeCycles();
        return targetCycles.contains(userCycle);
    }

    private long countHouseholdTypeMatches(User user, Welfare welfare) {
        Set<HouseholdType> userTypes = user.getHouseholdTypes();
        List<HouseholdType> targetTypes = welfare.getCategory().getHouseholdTypes();
        return targetTypes.stream()
                .filter(userTypes::contains)
                .count();
    }

    private boolean isRegionMatch(String userAddress, String ctpvNm, String sggNm) {
        if (userAddress == null || ctpvNm == null || sggNm == null) return false;

        String[] tokens = userAddress.split(" ");
        if (tokens.length < 2) return false;

        String userRegion = tokens[0] + " " + tokens[1];
        String targetRegion = ctpvNm + " " + sggNm;

        return userRegion.equals(targetRegion);
    }
}
