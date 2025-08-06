package com.dndn.backend.dndn.domain.welfare.domain.repository;

import com.dndn.backend.dndn.domain.welfare.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryNameAndParent(String categoryName, Category parent);
}
