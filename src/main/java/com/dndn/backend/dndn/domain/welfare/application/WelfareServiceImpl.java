package com.dndn.backend.dndn.domain.welfare.application;

import com.dndn.backend.dndn.domain.category.domain.enums.HouseholdType;
import com.dndn.backend.dndn.domain.category.domain.enums.InterestTopic;
import com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle;
import com.dndn.backend.dndn.domain.welfare.api.response.WelfareDetailResDto;
import com.dndn.backend.dndn.domain.welfare.api.response.WelfareListResDto;
import com.dndn.backend.dndn.domain.welfare.api.response.WelfareInfoResDto;
import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import com.dndn.backend.dndn.domain.welfare.domain.repository.WelfareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WelfareServiceImpl implements WelfareService {

    private final WelfareRepository welfareRepository;

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
                .orElseThrow(() -> new IllegalArgumentException("해당 복지 서비스를 찾을 수 없습니다."));
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
}
