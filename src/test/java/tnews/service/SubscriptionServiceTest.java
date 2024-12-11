
package tnews.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tnews.entity.KeyWord;
import tnews.entity.Subscription;
import tnews.repository.SubscriptionRepository;

@SpringBootTest
@ActiveProfiles("test")
class SubscriptionServiceTest {

  @Autowired
  SubscriptionService subscriptionService;
  @Autowired
  SubscriptionRepository subscriptionRepository;

  @Test
  void save() {
    Subscription subscription = new Subscription(
        null,
        Set.of(new KeyWord(1L, "keyWord1", Set.of()), new KeyWord(2L, "keyWord2", Set.of())),
        LocalDateTime.now(),
        Set.of(),
        LocalDateTime.now(),
        LocalDateTime.now()
    );

    Subscription persisted = subscriptionService.save(subscription);

    Subscription retrieved = subscriptionRepository.findById(persisted.getId()).orElse(null);

    assert Objects.requireNonNull(retrieved).getId() != null;
  }
}