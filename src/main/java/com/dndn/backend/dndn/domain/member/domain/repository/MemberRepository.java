package com.dndn.backend.dndn.domain.member.domain.repository;

import com.dndn.backend.dndn.domain.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
