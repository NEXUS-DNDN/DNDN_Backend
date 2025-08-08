package com.dndn.backend.dndn.domain.category.domain.enums;

import lombok.Getter;

@Getter
public enum HouseholdType {

    MULTICULTURAL("다문화·탈북민"),
    MULTI_CHILD("다자녀"),
    PATRIOT("보훈대상자"),
    DISABLED("장애인"),
    LOW_INCOME("저소득"),
    SINGLE_PARENT("한부모·조손");

    private final String kor;

    HouseholdType(String kor) {
        this.kor = kor;
    }

    public static HouseholdType fromKor(String kor) {
        for (HouseholdType value : values()) {
            if (value.kor.equals(kor)) {
                return value;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 가구유형: " + kor);
    }
}

