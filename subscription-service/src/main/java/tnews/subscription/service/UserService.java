package tnews.subscription.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tnews.aggregator.client.dto.NewsDto;
import tnews.subscription.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import tnews.subscription.entity.Category;
import tnews.subscription.entity.KeyWord;
import tnews.subscription.entity.Subscription;
import tnews.subscription.entity.User;
import tnews.subscription.entity.UserAction;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final KeyWordsService keyWordsService;
    private final SubscriptionService subscriptionService;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User create(User user) {
        return userRepository.findById(user.getId()).orElseGet(() -> userRepository.save(user));
    }

    public User update(Long id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User addCategory(Long id, String category) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        Category categoryObj = new Category();
        categoryObj.setCategoryName(category);
        Category savedCategory = categoryService.save(categoryObj);
        Subscription subscription = subscriptionService.addCategory(id, savedCategory);
        user.setSubscription(subscription);
        return userRepository.save(user);
    }

    public User addKeyword(Long id, String keyword) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        KeyWord keyWordObj = new KeyWord();
        keyWordObj.setKeyword(keyword);
        KeyWord savedKeyWord = keyWordsService.saveKeyWord(keyWordObj);
        Subscription subscription = subscriptionService.addKeyWord(id, savedKeyWord);

        user.setSubscription(subscription);
        user.setCurrentAction(UserAction.READY);
        return userRepository.save(user);
    }

    public User updateCurrentAction(Long id, String action) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        user.setCurrentAction(UserAction.valueOf(action));
        return userRepository.save(user);
    }

    public List<NewsDto> findActualNews(Long id) {
        updateCurrentAction(id, UserAction.READY.name());
        Subscription subscription = subscriptionService.findById(id);
        Set<String> categories = subscription.getCategories().stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toSet());
        List<List<NewsDto>> allNews = subscriptionService.getNewsByCategories(categories);
        Set<String> sendNews = subscription.getSentNewsIds();


        List<NewsDto> newNews = allNews.stream()
                .flatMap(Collection::stream)
                .filter(news -> !sendNews.contains(news.getId()))
                .limit(3)
                .toList();
        sendNews.addAll(newNews.stream()
                .map(NewsDto::getId)
                .toList());
        subscription.setSentNewsIds(sendNews);
        subscriptionService.save(subscription);
        return newNews;
    }

}
