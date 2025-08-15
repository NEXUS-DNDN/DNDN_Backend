package com.dndn.backend.dndn.domain.interest.domain.repository;


import com.dndn.backend.dndn.domain.interest.domain.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    // 유저-복지 조합 단건 조회
    Optional<Interest> findByUserIdAndWelfareId(Long userId, Long welfareId);

    // 선택 파라미터(status가 null이면 전체)
    @Query("""
       select distinct i
       from Interest i
       join fetch i.welfare w
       where i.user.id = :userId
         and (:status is null or i.interestStatus = :status)
       """)
    List<Interest> findWithOptionalStatus(@Param("userId") Long userId,
                                          @Param("status") Boolean status);

    // 상태만 변경
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Interest i set i.interestStatus = :status " +
            "where i.user.id = :userId and i.welfare.id = :welfareId")
    int updateStatus(@Param("userId") Long userId,
                     @Param("welfareId") Long welfareId,
                     @Param("status") Boolean status);
}
