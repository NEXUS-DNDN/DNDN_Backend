package com.dndn.backend.dndn.domain.category.domain.enums;

import lombok.Getter;

@Getter
public enum InterestTopic {

    PHYSICAL_HEALTH("신체건강"),
    MENTAL_HEALTH("정신건강"),
    LIVING_SUPPORT("생활지원"),
    HOUSING("주거"),
    JOB("일자리"),
    CULTURE("문화·여가"),
    SAFETY("안전·위기"),
    PREGNANT("임신·출산"),
    CHILD_CARE("보육"),
    EDUCATION("교육"),
    FOSTER_CARE("입양·위탁"),
    CARE("보호·돌봄"),
    FINANCE("서민금융"),
    LAW("법률"),
    ENERGY("에너지");

    private final String kor;

    InterestTopic(String kor) {
        this.kor = kor;
    }

    public static InterestTopic fromKor(String kor) {
        for (InterestTopic value : values()) {
            if (value.kor.equals(kor)) {
                return value;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 관심주제: " + kor);
    }
}

