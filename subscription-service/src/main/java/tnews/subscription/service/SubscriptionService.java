package tnews.subscription.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tnews.aggregator.client.AggregatorClient;
import tnews.aggregator.client.dto.NewsDto;
import tnews.aggregator.client.dto.NewsRequestDto;
import tnews.subscription.entity.Category;
import tnews.subscription.entity.KeyWord;
import tnews.subscription.entity.Subscription;
import tnews.subscription.repository.SubscriptionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class SubscriptionService {
    private SubscriptionRepository subscriptionRepository;
    private AggregatorClient aggregatorClient;

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

    public List<NewsDto> getActualNews(Long id, int limit) {
        Subscription subscription = findById(id);
        Set<String> categories = subscription.getCategories().stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toSet());
        Set<String> sentNewsIds = subscription.getSentNewsIds();
        List<NewsDto> actualNews = aggregatorClient.getActualNews(new NewsRequestDto(categories, sentNewsIds, limit));

        actualNews.stream()
                .map(NewsDto::getId)
                .forEach(sentNewsIds::add);

        subscription.setSentNewsIds(sentNewsIds);
        subscriptionRepository.save(subscription);

        return actualNews;
    }

    public List<Subscription> findNewsToValidSubscriptions() {
        List<Subscription> subscriptionList = subscriptionRepository.findValidSubscriptions();
        if (subscriptionList.isEmpty()) {
            log.info("Нет подписок для обновления времени отправки.");
            return List.of();
        }
        subscriptionList.forEach(subscription -> {subscription.setLastSend(LocalDateTime.now());});
        subscriptionRepository.saveAll(subscriptionList);
        log.info("Обновлено {} подписок, время отправки установлено на {}", subscriptionList.size(), LocalDateTime.now());
        return subscriptionList;
    }
}
