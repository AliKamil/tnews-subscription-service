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

    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    public Subscription findById(Long id) {
        return subscriptionRepository.findById(id).orElse(null);
    }

}
