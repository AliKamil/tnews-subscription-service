package tnews.subscription.mapper;

import tnews.subscription.entity.Category;

public class CategoryMapper {
    public static Category fromString(String categoryString) {
        Category category = new Category();
        category.setCategoryName(categoryString);
        return category;
    }

}
