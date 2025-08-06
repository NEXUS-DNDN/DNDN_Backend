package com.dndn.backend.dndn.domain.welfare.domain.repository;

import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WelfareRepository extends JpaRepository<Welfare, Long> {

    List<Welfare> findByTitleContaining(String keyword);
    Optional<Welfare> findByServId(String servId);

}
