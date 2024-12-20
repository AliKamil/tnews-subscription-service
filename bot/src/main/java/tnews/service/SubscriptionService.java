package tnews.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tnews.entity.Subscription;
import tnews.repository.SubscriptionRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SubscriptionService {
    private SubscriptionRepository subscriptionRepository;
    private CategoryService categoryService;

    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    public Subscription findById(Long id) {
        return subscriptionRepository.findById(id).orElse(null);
    }

//    public void addCategory(Long id, String category) {
//        Category categoryObj = new Category();
//        categoryObj.setCategoryName(category);
//        Subscription subscription = new Subscription();
//        subscription.setId(id);
//        subscription.setCategories(Set.of(categoryObj));
//        subscriptionRepository.save(subscription);
//    }

    public Subscription save(Subscription subscription) {
//        if (subscription.getCategories() != null) {
//            for(Category category : subscription.getCategories()) {
//                if (categoryService.findByCategoryName(category.getCategoryName()) != null) {
//
//                }
//            }
//        }
        return subscriptionRepository.save(subscription);
    }
}
