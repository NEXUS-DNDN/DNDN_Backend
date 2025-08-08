package com.dndn.backend.dndn.domain.category.util;

import com.dndn.backend.dndn.domain.category.domain.enums.HouseholdType;
import com.dndn.backend.dndn.domain.category.domain.enums.InterestTopic;
import com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle;

import java.util.Arrays;
import java.util.List;

public class CategoryParserUtils {

    private CategoryParserUtils() { }

    public static List<LifeCycle> parseLifeCycles(String raw) {
        if (raw == null || raw.isBlank()) return List.of();
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .map(LifeCycle::fromKor)
                .toList();
    }

    public static List<HouseholdType> parseHouseholdTypes(String raw) {
        if (raw == null || raw.isBlank()) return List.of();
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .map(HouseholdType::fromKor)
                .toList();
    }

    public static List<InterestTopic> parseInterestTopics(String raw) {
        if (raw == null || raw.isBlank()) return List.of();
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .map(InterestTopic::fromKor)
                .toList();
    }
}

