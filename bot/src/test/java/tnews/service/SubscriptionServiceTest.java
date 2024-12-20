
package tnews.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tnews.entity.Category;
import tnews.entity.KeyWord;
import tnews.entity.Subscription;
import tnews.repository.CategoryRepository;
import tnews.repository.KeyWordRepository;
import tnews.repository.SubscriptionRepository;

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

  @BeforeEach
  void setUp() {
    KeyWord keyWord1 = new KeyWord(null, "ONE", Set.of());
    KeyWord keyWord2 = new KeyWord(null, "TWO", Set.of());
    KeyWord keyWord3 = new KeyWord(null, "THREE", Set.of());

    KeyWord savedKeyWord = keyWordRepository.save(keyWord1);
    KeyWord savedKeyWord2 = keyWordRepository.save(keyWord2);
    KeyWord savedKeyWord3 = keyWordRepository.save(keyWord3);

    keyWordId1 = savedKeyWord.getId();
    keyWordId2 = savedKeyWord2.getId();
    keyWordId3 = savedKeyWord3.getId();

    Category category1 = new Category(null, "category1", Set.of());
    Category category2 = new Category(null, "category2", Set.of());
    Category category3 = new Category(null, "category3", Set.of());

    Category savedCategory = categoryRepository.save(category1);
    Category savedCategory2 = categoryRepository.save(category2);
    Category savedCategory3 = categoryRepository.save(category3);

    categoryId1 = savedCategory.getId();
    categoryId2 = savedCategory2.getId();
    categoryId3 = savedCategory3.getId();

//    Subscription subscription1 = new Subscription(
//            null,
//            Set.of(keyWord1, keyWord2, keyWord3),
//            LocalDateTime.now(),
//            Set.of(category1, category2, category3),
//            LocalDateTime.now(),
//            LocalDateTime.now()
//    );
//    Subscription subscription2 = new Subscription(
//            null,
//            Set.of(),
//            LocalDateTime.now(),
//            Set.of(category1),
//            LocalDateTime.now(),
//            LocalDateTime.now()
//    );
//    Subscription subscription3 = new Subscription(
//            null,
//            Set.of(keyWord3, keyWord1),
//            LocalDateTime.now(),
//            Set.of(),
//            LocalDateTime.now(),
//            LocalDateTime.now()
//    );
//    Subscription savedSubscription = subscriptionRepository.save(subscription1);
//    Subscription savedSubscription2 = subscriptionRepository.save(subscription2);
//    Subscription savedSubscription3 = subscriptionRepository.save(subscription3);
//
//    subscriptionId1 = savedSubscription.getId();
//    subscriptionId2 = savedSubscription2.getId();
//    subscriptionId3 = savedSubscription3.getId();

  }

  @AfterEach
  void tearDown() {
//    subscriptionRepository.deleteById(subscriptionId1);
//    subscriptionRepository.deleteById(subscriptionId2);
//    subscriptionRepository.deleteById(subscriptionId3);

    keyWordRepository.deleteById(keyWordId1);
    keyWordRepository.deleteById(keyWordId2);
    keyWordRepository.deleteById(keyWordId3);

    categoryRepository.deleteById(categoryId1);
    categoryRepository.deleteById(categoryId2);
    categoryRepository.deleteById(categoryId3);

  }


  @Test
  void save() {
    Subscription subscription = new Subscription(
        null,
        Set.of(Objects.requireNonNull(keyWordRepository.findById(keyWordId1).orElse(null)),
                Objects.requireNonNull(keyWordRepository.findById(keyWordId2).orElse(null))),
        LocalDateTime.now(),
        Set.of(Objects.requireNonNull(categoryRepository.findById(categoryId1).orElse(null)),
                Objects.requireNonNull(categoryRepository.findById(categoryId2).orElse(null))),
        LocalDateTime.now(),
        LocalDateTime.now()
    );

    Subscription persisted = subscriptionService.save(subscription);
    Subscription retrieved = subscriptionRepository.findById(persisted.getId()).orElse(null);
    assertNotNull(persisted);
    assertNotNull(retrieved);
    assertEquals(persisted.getId(), retrieved.getId());
    assertEquals(persisted.getCategories().size(), retrieved.getCategories().size());
//    for(int i = 0; i<persisted.getCategories().size(); i++) {
//      assertEquals(
//              persisted.getCategories().stream()
//                      .sorted()
//                      .toList()
//                      .get(i),
//              retrieved.getCategories().stream()
//                      .sorted()
//                      .toList()
//                      .get(i)
//      );
//    }
    assertEquals(persisted.getKeyWords().size(), retrieved.getKeyWords().size());
//    for(int i = 0; i < persisted.getKeyWords().size(); i++) {
//      assertEquals(
//              persisted.getKeyWords().stream()
//                      .sorted()
//                      .toList()
//                      .get(i),
//              retrieved.getKeyWords().stream()
//                      .sorted()
//                      .toList()
//                      .get(i)
//      );
//    }
    assertEquals(
            persisted.getTimeInterval().truncatedTo(ChronoUnit.MILLIS),
            retrieved.getTimeInterval().truncatedTo(ChronoUnit.MILLIS)
    );


  }
}