package com.dndn.backend.dndn.domain.member.domain.repository;

import com.dndn.backend.dndn.domain.member.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
