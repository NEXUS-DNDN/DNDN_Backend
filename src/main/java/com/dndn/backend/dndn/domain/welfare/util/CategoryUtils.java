package com.dndn.backend.dndn.domain.welfare.util;

import com.dndn.backend.dndn.domain.welfare.domain.Category;

import java.util.ArrayList;
import java.util.List;

// 상하위 카테고리 모두 포함해서 전달하기 위한 클래스
public class CategoryUtils {

    private CategoryUtils() { }

    public static List<String> extractCategoryNames(Category category) {
        List<String> names = new ArrayList<>();
        if (category.getParent() != null) {
            names.add(category.getParent().getCategoryName());
        }
        names.add(category.getCategoryName());
        return names;
    }
}

