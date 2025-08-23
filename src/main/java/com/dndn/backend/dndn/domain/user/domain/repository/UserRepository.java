package com.dndn.backend.dndn.domain.user.domain.repository;

import com.dndn.backend.dndn.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<com.dndn.backend.dndn.domain.user.domain.entity.User> findBySocialId(String socialId);

    boolean existsBySocialId(String socialId);
}
