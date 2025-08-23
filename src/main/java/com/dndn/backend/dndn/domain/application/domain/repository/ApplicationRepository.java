package com.dndn.backend.dndn.domain.application.domain.repository;

import com.dndn.backend.dndn.domain.application.domain.Application;
import com.dndn.backend.dndn.domain.application.domain.enums.ReceiveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("""
    select distinct a
    from Application a
    join fetch a.welfare w
    join fetch w.category c
    left join fetch c.lifeCycles lc
    where a.user.id = :userId
    and a.receiveStatus = :status
    order by a.appliedAt desc
    """)
    List<Application> findAllWithWelfareByUserAndStatus(
            @Param("userId") Long userId,
            @Param("status") ReceiveStatus status
    );

    boolean existsByUser_IdAndWelfare_Id(Long userId, Long welfareId);

    int deleteByIdAndUser_IdAndReceiveStatus(Long id, Long userId, ReceiveStatus status);
}

