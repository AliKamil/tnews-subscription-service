
package tnews.subscription.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tnews.subscription.entity.Category;
import tnews.subscription.entity.KeyWord;
import tnews.subscription.entity.Subscription;
import tnews.subscription.entity.TimeInterval;
import tnews.subscription.repository.CategoryRepository;
import tnews.subscription.repository.KeyWordRepository;
import tnews.subscription.repository.SubscriptionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SubscriptionServiceTest {

  @Autowired
  SubscriptionService subscriptionService;
  @Autowired
  SubscriptionRepository subscriptionRepository;
  @Autowired
  KeyWordRepository keyWordRepository;
  @Autowired
  KeyWordsService keyWordsService;
  @Autowired
  CategoryRepository categoryRepository;

  Long subscriptionId1;
  Long subscriptionId2;
  Long subscriptionId3;

  Long keyWordId1;
  Long keyWordId2;
  Long keyWordId3;

  Long categoryId1;
  Long categoryId2;
  Long categoryId3;
    @Autowired
    private CategoryService categoryService;

  @BeforeEach
  void setUp() {
    KeyWord keyWord1 = new KeyWord(null, "ONE", Set.of());
    KeyWord keyWord2 = new KeyWord(null, "TWO", Set.of());
    KeyWord keyWord3 = new KeyWord(null, "THREE", Set.of());

    KeyWord savedKeyWord = keyWordsService.saveKeyWord(keyWord1);
    KeyWord savedKeyWord2 = keyWordsService.saveKeyWord(keyWord2);
    KeyWord savedKeyWord3 = keyWordsService.saveKeyWord(keyWord3);

    keyWordId1 = savedKeyWord.getId();
    keyWordId2 = savedKeyWord2.getId();
    keyWordId3 = savedKeyWord3.getId();

    Category category1 = new Category(null, "category1", Set.of());
    Category category2 = new Category(null, "category2", Set.of());
    Category category3 = new Category(null, "category3", Set.of());

    Category savedCategory = categoryService.save(category1);
    Category savedCategory2 = categoryService.save(category2);
    Category savedCategory3 = categoryService.save(category3);

    categoryId1 = savedCategory.getId();
    categoryId2 = savedCategory2.getId();
    categoryId3 = savedCategory3.getId();

    Subscription subscription1 = new Subscription(
            1L, //TODO: id должен быть такой же как у User из-за связи OneToOne (не может быть подписки без пользователя)
            Set.of(savedKeyWord, savedKeyWord2, savedKeyWord3),
            TimeInterval.ONE_DAY,
            null,
            Set.of(savedCategory, savedCategory2, savedCategory3),
            null,
            LocalDateTime.now(),
            LocalDateTime.now()
    );
    Subscription subscription2 = new Subscription(
            2L,
            Set.of(),
            TimeInterval.ONE_MONTH,
            null,
            Set.of(savedCategory),
            null,
            LocalDateTime.now(),
            LocalDateTime.now()
    );
    Subscription subscription3 = new Subscription(
            3L,
            Set.of(savedKeyWord3, savedKeyWord),
            TimeInterval.ONE_MONTH,
            null,
            Set.of(),
            null,
            LocalDateTime.now(),
            LocalDateTime.now()
    );
    Subscription savedSubscription = subscriptionRepository.save(subscription1);
    Subscription savedSubscription2 = subscriptionRepository.save(subscription2);
    Subscription savedSubscription3 = subscriptionRepository.save(subscription3);

    subscriptionId1 = savedSubscription.getId();
    subscriptionId2 = savedSubscription2.getId();
    subscriptionId3 = savedSubscription3.getId();

  }

  @AfterEach
  void tearDown() {
    subscriptionRepository.deleteById(subscriptionId1);
    subscriptionRepository.deleteById(subscriptionId2);
    subscriptionRepository.deleteById(subscriptionId3);

//    keyWordRepository.deleteById(keyWordId1);
//    keyWordRepository.deleteById(keyWordId2);
//    keyWordRepository.deleteById(keyWordId3);

//    categoryRepository.deleteById(categoryId1);
//    categoryRepository.deleteById(categoryId2);
//    categoryRepository.deleteById(categoryId3);



  }


  @Test
  void save() {
    Subscription subscription = new Subscription(
        4L,
        Set.of(Objects.requireNonNull(keyWordRepository.findById(keyWordId1).orElse(null)),
                Objects.requireNonNull(keyWordRepository.findById(keyWordId2).orElse(null))),
        TimeInterval.ONE_HOUR,
        null,
        Set.of(Objects.requireNonNull(categoryRepository.findById(categoryId1).orElse(null)),
                Objects.requireNonNull(categoryRepository.findById(categoryId2).orElse(null))),
        null,
        LocalDateTime.now(),
        LocalDateTime.now()
    );

    Subscription persisted = subscriptionService.save(subscription);
    Subscription retrieved = subscriptionRepository.findById(persisted.getId()).orElse(null);
    assertNotNull(persisted);
    assertNotNull(retrieved);
    assertEquals(persisted.getId(), retrieved.getId());
    assertEquals(persisted.getTimeInterval(), retrieved.getTimeInterval());

    Long id = retrieved.getId();
    subscriptionRepository.deleteById(id);
    assertNull(subscriptionRepository.findById(id).orElse(null));
  }

  @Test
  public void getAllSubscriptions() {
    List<Subscription> subscriptions = subscriptionService.findAll();
    assertNotNull(subscriptions);
    List<Subscription> retrievedSubscriptions = subscriptionRepository.findAll();
    assertNotNull(retrievedSubscriptions);
    assertEquals(subscriptions.size(), retrievedSubscriptions.size());
    for (int i = 0; i < subscriptions.size(); i++) {
      assertEquals(subscriptions.get(i).getId(), retrievedSubscriptions.get(i).getId());
      assertEquals(subscriptions.get(i).getTimeInterval(), retrievedSubscriptions.get(i).getTimeInterval());
    }
  }

  @Test
  public void getSubscriptionById() {
    Subscription subscription = subscriptionService.findById(subscriptionId1);
    assertNotNull(subscription);
    Subscription retrieved = subscriptionRepository.findById(subscriptionId1).orElse(null);
    assertNotNull(retrieved);

    assertEquals(subscription.getId(), retrieved.getId());
    assertEquals(subscription.getTimeInterval(), retrieved.getTimeInterval());
  }

}