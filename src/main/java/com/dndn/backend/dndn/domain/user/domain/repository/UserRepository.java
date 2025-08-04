package com.dndn.backend.dndn.domain.user.domain.repository;

import com.dndn.backend.dndn.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
