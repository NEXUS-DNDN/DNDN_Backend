package com.dndn.backend.dndn.domain.welfare.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CATEGORY")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "category_name", length = 20, nullable = false)
    private String categoryName;

    // 상위 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parent;

    // 하위 카테고리들
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();

    @Builder
    public Category(String categoryName, Category parent) {
        this.categoryName = categoryName;
        this.parent = parent;
    }

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Welfare> welfares = new ArrayList<>();

    public void updateName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void changeParent(Category parent) {
        this.parent = parent;
    }
}

