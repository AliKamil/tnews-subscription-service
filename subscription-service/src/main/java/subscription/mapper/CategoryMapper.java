package subscription.mapper;

import subscription.entity.Category;

public class CategoryMapper {
    public static Category fromString(String categoryString) {
        Category category = new Category();
        category.setCategoryName(categoryString);
        return category;
    }

}
