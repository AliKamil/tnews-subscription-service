package subscription.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import subscription.entity.Category;
import subscription.entity.KeyWord;
import subscription.entity.Subscription;
import subscription.repository.SubscriptionRepository;

import java.util.List;
import java.util.Set;

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

    public Subscription addCategory(Long id, Category category) {
        Set<Category> categories;
        Subscription subscription = findById(id);
        if (subscription != null) {
            categories = subscription.getCategories();
            categories.add(category);
        } else {
            subscription = new Subscription();
            subscription.setId(id);
            categories = Set.of(category);
        }
        subscription.setCategories(categories);
        return subscriptionRepository.save(subscription);
    }

    public Subscription addKeyWord(Long id, KeyWord keyword) {
        Set<KeyWord> keyWords;
        Subscription subscription = findById(id);
        if (subscription != null) {
            keyWords = subscription.getKeyWords();
            keyWords.add(keyword);
        } else {
            subscription = new Subscription();
            subscription.setId(id);
            keyWords = Set.of(keyword);
        }
        subscription.setKeyWords(keyWords);
        return subscriptionRepository.save(subscription);
    }

    public Subscription save(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }
    public void deleteById(Long id) {
        subscriptionRepository.deleteById(id);
    }

//    public Set<Category> getCategoriesBySubscriptionId(Long id) {
//        Subscription subscription = findById(id);
//        Hibernate.initialize(subscription.getCategories());
//        return subscription.getCategories();
//    }

}
