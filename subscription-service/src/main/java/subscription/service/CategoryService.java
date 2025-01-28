package subscription.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import subscription.entity.Category;
import subscription.repository.CategoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private CategoryRepository categoryRepository;

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



}
