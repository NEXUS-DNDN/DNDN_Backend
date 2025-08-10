package com.dndn.backend.dndn.domain.category.application;

import com.dndn.backend.dndn.domain.category.domain.Category;
import com.dndn.backend.dndn.domain.category.domain.enums.HouseholdType;
import com.dndn.backend.dndn.domain.category.domain.enums.InterestTopic;
import com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle;
import com.dndn.backend.dndn.domain.category.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    // 카테고리 비교 로직
    public Optional<Category> findMatchingCategory(List<LifeCycle> lifeCycles,
                                                    List<HouseholdType> householdTypes,
                                                    List<InterestTopic> interestTopics) {
        return categoryRepository.findAll().stream()
                .filter(c -> c.getLifeCycles().equals(lifeCycles)
                        && c.getHouseholdTypes().equals(householdTypes)
                        && c.getInterestTopics().equals(interestTopics))
                .findFirst();
    }

    public Category findOrCreateCategory(List<LifeCycle> lifeCycles,
                                         List<HouseholdType> householdTypes,
                                         List<InterestTopic> interestTopics) {
        return findMatchingCategory(lifeCycles, householdTypes, interestTopics)
                .orElseGet(() -> {
                    Category newCategory = Category.builder()
                            .lifeCycles(lifeCycles)
                            .householdTypes(householdTypes)
                            .interestTopics(interestTopics)
                            .build();
                    return categoryRepository.save(newCategory);
                });
    }
}
