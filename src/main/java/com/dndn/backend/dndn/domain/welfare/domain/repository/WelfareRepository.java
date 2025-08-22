package com.dndn.backend.dndn.domain.welfare.domain.repository;

import com.dndn.backend.dndn.domain.category.domain.enums.HouseholdType;
import com.dndn.backend.dndn.domain.category.domain.enums.InterestTopic;
import com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle;
import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WelfareRepository extends JpaRepository<Welfare, Long> {

    List<Welfare> findByTitleContaining(String keyword);
    Optional<Welfare> findByServId(String servId);

    @Query("""
    select distinct w
    from Welfare w
      join w.category c
      join c.lifeCycles lc
      left join c.householdTypes hh
      left join c.interestTopics it
    where lc = :lifeCycle
      and (:householdsEmpty = true or hh in :households)
      and (:interestsEmpty = true or it in :interests)
    """)
    List<Welfare> findByCategoryFilters(
            @Param("lifeCycle") LifeCycle lifeCycle,
            @Param("households") List<HouseholdType> households,
            @Param("householdsEmpty") boolean householdsEmpty,
            @Param("interests") List<InterestTopic> interests,
            @Param("interestsEmpty") boolean interestsEmpty
    );

    @Query("""
    select distinct w
    from Welfare w
    join w.category c
    join c.lifeCycles lc
    left join c.householdTypes hh
    left join c.interestTopics it
    where (
        lower(coalesce(w.title, '')) like concat('%', lower(:keyword), '%')
        or lower(concat(coalesce(w.content, ''), '')) like concat('%', lower(:keyword), '%')
    )
    and lc = :lifeCycle
    and (:householdsEmpty = true or hh in :households)
    and (:interestsEmpty = true or it in :interests)
    """)
    List<Welfare> searchByKeywordAndCategory(
            @Param("keyword") String keyword,
            @Param("lifeCycle") LifeCycle lifeCycle,
            @Param("households") List<HouseholdType> households,
            @Param("householdsEmpty") boolean householdsEmpty,
            @Param("interests") List<InterestTopic> interests,
            @Param("interestsEmpty") boolean interestsEmpty
    );
}
