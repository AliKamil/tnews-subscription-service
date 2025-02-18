package tnews.subscription.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tnews.subscription.entity.Category;
import tnews.subscription.repository.CategoryRepository;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    Long categoryId1;
    Long categoryId2;
    Long categoryId3;

    @BeforeEach
    void setUp() {
        Category category1 = new Category(null, "category1", Set.of());
        Category category2 = new Category(null, "category2", Set.of());
        Category category3 = new Category(null, "category3", Set.of());

        Category savedCategory = categoryRepository.save(category1);
        Category savedCategory2 = categoryRepository.save(category2);
        Category savedCategory3 = categoryRepository.save(category3);

        categoryId1 = savedCategory.getId();
        categoryId2 = savedCategory2.getId();
        categoryId3 = savedCategory3.getId();
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteById(categoryId1);
        categoryRepository.deleteById(categoryId2);
        categoryRepository.deleteById(categoryId3);
    }

    @Test
    void save() {
        Category category = new Category(null, "NEW_category", Set.of());
        Category savedCategory = categoryService.save(category);
        assertNotNull(savedCategory);
        Category retrievedCategory = categoryRepository.findById(savedCategory.getId()).orElse(null);
        assertNotNull(retrievedCategory);
        assertEquals(savedCategory.getId(), retrievedCategory.getId());
        assertEquals(savedCategory.getCategoryName(), retrievedCategory.getCategoryName());
        Long id = retrievedCategory.getId();
        categoryRepository.deleteById(id);
        assertNull(categoryRepository.findById(id).orElse(null));
    }

    @Test
    void findAll() {
        List<Category> categories = categoryService.findAll();
        List<Category> retrievedCategories = categoryRepository.findAll();
        assertNotNull(categories);
        assertNotNull(retrievedCategories);
        assertEquals(categories.size(), retrievedCategories.size());
        for(int i = 0; i < categories.size(); i++) {
            assertEquals(categories.get(i).getCategoryName(), retrievedCategories.get(i).getCategoryName());
        }
        Category category1 = new Category(null, "category1", Set.of());
        categoryService.save(category1);
        List<Category> categories1 = categoryService.findAll();
        assertNotNull(categories1);
        assertEquals(categories.size(), categories1.size());
    }

    @Test
    void findById() {
        Category category = categoryService.findById(categoryId1);
        assertNotNull(category);
        Category retrievedCategory = categoryRepository.findById(categoryId1).orElse(null);
        assertNotNull(retrievedCategory);
        assertEquals(category.getId(), retrievedCategory.getId());
        assertEquals(category.getCategoryName(), retrievedCategory.getCategoryName());
    }


}