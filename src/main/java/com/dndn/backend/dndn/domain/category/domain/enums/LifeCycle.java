package com.dndn.backend.dndn.domain.category.domain.enums;

import lombok.Getter;

@Getter
public enum LifeCycle {

    INFANT("영유아"),
    CHILD("아동"),
    TEENAGER("청소년"),
    YOUTH("청년"),
    MIDDLE("중장년"),
    SENIOR("노년"),
    PREGNANT("임신·출산");

    private final String kor;

    LifeCycle(String kor) {
        this.kor = kor;
    }

    public static LifeCycle fromKor(String kor) {
        if (kor == null) throw new IllegalArgumentException("null 값 들어옴");

        String normalized = kor.replace(" ", "").trim();
        for (LifeCycle value : values()) {
            if (value.kor.replace(" ", "").equals(normalized)) {
                return value;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 생애주기: " + kor);
    }

}

