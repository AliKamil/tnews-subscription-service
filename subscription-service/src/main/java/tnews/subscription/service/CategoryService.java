package tnews.subscription.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tnews.aggregator.client.AggregatorClient;
import tnews.subscription.entity.Category;
import tnews.subscription.mapper.CategoryMapper;
import tnews.subscription.repository.CategoryRepository;

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
