package subscription.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subscription.client.AggregatorClient;
import subscription.entity.Category;
import subscription.mapper.CategoryMapper;
import subscription.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final AggregatorClient aggregatorClient;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category save(Category category) {
        Category find = categoryRepository.findByCategoryName(category.getCategoryName());
        if (find == null)
            return categoryRepository.save(category);
        return find;
    }

    public Category findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    public List<Category> updateCategories() {
        List<String> categories = aggregatorClient.getCategories();
        for (String categoryName : categories) {
            save(CategoryMapper.fromString(categoryName));
        }
        return findAll();
    }



}
