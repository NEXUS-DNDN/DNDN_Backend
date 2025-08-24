package com.dndn.backend.dndn.domain.user.domain.repository;

import com.dndn.backend.dndn.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<com.dndn.backend.dndn.domain.user.domain.entity.User> findBySocialId(String socialId);

    boolean existsBySocialId(String socialId);

    @Query("""
    select u from User u
    left join fetch u.seniorInfo
    left join fetch u.disabledInfo
    left join fetch u.householdTypes
    where u.id = :userId
""")
    Optional<User> findWithAllAdditionalInfoById(@Param("userId") Long userId);


}
