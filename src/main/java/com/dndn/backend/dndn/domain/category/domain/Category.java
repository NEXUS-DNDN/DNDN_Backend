package com.dndn.backend.dndn.domain.category.domain;

import com.dndn.backend.dndn.domain.category.domain.enums.HouseholdType;
import com.dndn.backend.dndn.domain.category.domain.enums.InterestTopic;
import com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CATEGORY")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 생애주기
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "category_lifecycle", joinColumns = @JoinColumn(name = "category_id"))
    @Column(name = "life_cycle")
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 100)
    private List<LifeCycle> lifeCycles = new ArrayList<>();

    // 가구유형
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "category_household", joinColumns = @JoinColumn(name = "category_id"))
    @Column(name = "household_type")
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 100)
    private List<HouseholdType> householdTypes = new ArrayList<>();

    // 관심주제
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "category_interest", joinColumns = @JoinColumn(name = "category_id"))
    @Column(name = "interest_topic")
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 100)
    private List<InterestTopic> interestTopics = new ArrayList<>();

    @Builder
    public Category(List<LifeCycle> lifeCycles,
                    List<HouseholdType> householdTypes,
                    List<InterestTopic> interestTopics) {
        this.lifeCycles = lifeCycles;
        this.householdTypes = householdTypes;
        this.interestTopics = interestTopics;
    }

}

